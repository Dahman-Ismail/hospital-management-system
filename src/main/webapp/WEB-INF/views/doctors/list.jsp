<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="flex justify-between items-center mb-6">
  <h2 class="text-3xl font-bold text-gray-800">Doctors</h2>

  <div class="flex gap-2">
    <!-- Export Button -->
    <button
      onclick="exportTableToExcel('doctorsTable')"
      class="bg-green-600 text-white px-4 py-2 rounded hover:bg-green-500 transition"
    >
      Download Excel
    </button>

    <a
      href="/doctors/create"
      class="bg-indigo-600 text-white px-4 py-2 rounded hover:bg-indigo-500 transition"
    >
      Add New Doctor
    </a>
  </div>
</div>

<!-- Search Form -->
<form method="get" action="/doctors/search/name" class="mb-4 flex gap-2">
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

<!-- Doctors Table -->
<div class="overflow-x-auto bg-white shadow rounded-lg">
  <!-- Add table ID -->
  <table id="doctorsTable" class="min-w-full divide-y divide-gray-200">
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
          Specialization
        </th>
        <th
          class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase"
        >
          Phone
        </th>
        <th
          class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase"
        >
          Email
        </th>

        <!-- Counters -->
        <th
          class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase"
        >
          Total
        </th>
        <th
          class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase"
        >
          Planifie
        </th>
        <th
          class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase"
        >
          Termine
        </th>
        <th
          class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase"
        >
          Annule
        </th>

        <th
          class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase"
        >
          Actions
        </th>
      </tr>
    </thead>

    <tbody class="bg-white divide-y divide-gray-200">
      <c:forEach var="doctor" items="${doctors}">
        <tr class="hover:bg-gray-50">
          <td class="px-6 py-4">${doctor.doctorId}</td>
          <td class="px-6 py-4">${doctor.name}</td>
          <td class="px-6 py-4">${doctor.specialization}</td>
          <td class="px-6 py-4">${doctor.phone}</td>
          <td class="px-6 py-4">${doctor.email}</td>

          <!-- Appointment Counters -->
          <td class="px-6 py-4 font-semibold">${doctor.totalAppointments}</td>
          <td class="px-6 py-4 text-blue-600">${doctor.plannedAppointments}</td>
          <td class="px-6 py-4 text-green-600">
            ${doctor.completedAppointments}
          </td>
          <td class="px-6 py-4 text-red-600">
            ${doctor.cancelledAppointments}
          </td>

          <td class="px-6 py-4 flex gap-2">
            <a
              href="/doctors/edit/${doctor.id}"
              class="text-indigo-600 hover:text-indigo-900"
              >Edit</a
            >
            <a
              href="/doctors/delete/${doctor.id}"
              class="text-red-600 hover:text-red-900"
              onclick="return confirm('Are you sure you want to delete this doctor?')"
            >
              Delete
            </a>
          </td>
        </tr>
      </c:forEach>
    </tbody>
  </table>
</div>

<!-- EXPORT SCRIPT -->
<script>
  function exportTableToExcel(tableID, filename = "") {
    let dataType = "application/vnd.ms-excel";
    let tableSelect = document.getElementById(tableID);
    let tableHTML = tableSelect.outerHTML.replace(/ /g, "%20");

    filename = filename ? filename + ".xls" : "doctors.xls";

    let downloadLink = document.createElement("a");
    document.body.appendChild(downloadLink);

    if (navigator.msSaveOrOpenBlob) {
      // IE fallback
      let blob = new Blob(["\ufeff", tableHTML], { type: dataType });
      navigator.msSaveOrOpenBlob(blob, filename);
    } else {
      // For modern browsers
      downloadLink.href = "data:" + dataType + ", " + tableHTML;
      downloadLink.download = filename;
      downloadLink.click();
    }
  }
</script>
