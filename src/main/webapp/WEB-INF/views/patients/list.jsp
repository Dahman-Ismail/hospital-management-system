<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="flex justify-between items-center mb-6">
  <h2 class="text-3xl font-bold text-gray-800">Patients</h2>
  <a
    href="/patients/create"
    class="bg-indigo-600 text-white px-4 py-2 rounded hover:bg-indigo-500 transition"
  >
    Add New Patient
  </a>
</div>

<!-- Search Form -->
<form method="get" action="/patients/search" class="mb-4 flex gap-2">
  <input
    type="text"
    name="name"
    placeholder="Search by name"
    value="${searchTerm}"
    class="flex-1 border border-gray-300 rounded px-3 py-2 focus:outline-none focus:ring-2 focus:ring-indigo-500"
  />
  <button
    type="submit"
    class="bg-indigo-600 text-white px-4 py-2 rounded hover:bg-indigo-500"
  >
    Search
  </button>
</form>

<!-- Patients Table -->
<div class="overflow-x-auto bg-white shadow rounded-lg">
  <table class="min-w-full divide-y divide-gray-200">
    <thead class="bg-indigo-50">
      <tr>
        <th
          class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase"
        >
          ID
        </th>
        <th
          class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase"
        >
          Name
        </th>
        <th
          class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase"
        >
          DOB
        </th>
        <th
          class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase"
        >
          Gender
        </th>
        <th
          class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase"
        >
          Phone
        </th>
        <th
          class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase"
        >
          Actions
        </th>
      </tr>
    </thead>
    <tbody class="bg-white divide-y divide-gray-200">
      <c:forEach var="patient" items="${patients}">
        <tr class="hover:bg-gray-50">
          <td class="px-6 py-4">${patient.patientId}</td>
          <td class="px-6 py-4">${patient.name}</td>
          <td class="px-6 py-4">${patient.dob}</td>
          <td class="px-6 py-4">${patient.gender}</td>
          <td class="px-6 py-4">${patient.phone}</td>
          <td class="px-6 py-4 flex gap-2">
            <a
              href="/patients/edit/${patient.id}"
              class="text-indigo-600 hover:text-indigo-900"
              >Edit</a
            >
            <a
              href="/patients/delete/${patient.id}"
              class="text-red-600 hover:text-red-900"
              onclick="return confirm('Are you sure you want to delete this patient?')"
              >Delete</a
            >
          </td>
        </tr>
      </c:forEach>
    </tbody>
  </table>
</div>
