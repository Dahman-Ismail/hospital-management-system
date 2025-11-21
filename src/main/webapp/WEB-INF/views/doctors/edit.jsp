<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="max-w-xl mx-auto bg-white shadow-md rounded-lg p-6">
  <h2 class="text-2xl font-bold mb-4">Edit Doctor</h2>

  <form method="post" action="/doctors/edit/${doctor.id}" class="space-y-4">
    <div>
      <label class="block text-gray-700">Doctor ID</label>
      <input
        type="text"
        name="doctorId"
        value="${doctor.doctorId}"
        required
        class="w-full border border-gray-300 rounded px-3 py-2 focus:outline-none focus:ring-2 focus:ring-indigo-500"
      />
    </div>
    <div>
      <label class="block text-gray-700">Name</label>
      <input
        type="text"
        name="name"
        value="${doctor.name}"
        required
        class="w-full border border-gray-300 rounded px-3 py-2 focus:outline-none focus:ring-2 focus:ring-indigo-500"
      />
    </div>
    <div>
      <label class="block text-gray-700">Specialization</label>
      <input
        type="text"
        name="specialization"
        value="${doctor.specialization}"
        required
        class="w-full border border-gray-300 rounded px-3 py-2 focus:outline-none focus:ring-2 focus:ring-indigo-500"
      />
    </div>
    <div>
      <label class="block text-gray-700">Phone</label>
      <input
        type="text"
        name="phone"
        value="${doctor.phone}"
        required
        class="w-full border border-gray-300 rounded px-3 py-2 focus:outline-none focus:ring-2 focus:ring-indigo-500"
      />
    </div>
    <div>
      <label class="block text-gray-700">Email</label>
      <input
        type="email"
        name="email"
        value="${doctor.email}"
        class="w-full border border-gray-300 rounded px-3 py-2 focus:outline-none focus:ring-2 focus:ring-indigo-500"
      />
    </div>
    <div>
      <label class="block text-gray-700">Working Days (comma separated)</label>
      <input
        type="text"
        name="workingDays"
        value="<c:if test='${doctor.workingDays != null}'><c:forEach var='day' items='${doctor.workingDays}' varStatus='status'>${day}<c:if test='${!status.last}'>, </c:if></c:forEach></c:if>"
        class="w-full border border-gray-300 rounded px-3 py-2 focus:outline-none focus:ring-2 focus:ring-indigo-500"
      />
    </div>

    <div class="flex justify-end gap-2">
      <a href="/doctors" class="px-4 py-2 bg-gray-300 rounded hover:bg-gray-400"
        >Cancel</a
      >
      <button
        type="submit"
        class="px-4 py-2 bg-indigo-600 text-white rounded hover:bg-indigo-500"
      >
        Update
      </button>
    </div>
  </form>
</div>
