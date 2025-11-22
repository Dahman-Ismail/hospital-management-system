<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="flex justify-between items-center mb-4">
  <h2 class="text-2xl font-bold">Appointments</h2>

  <div class="space-x-2">
    <!-- Export Button -->
    <button onclick="exportTableToExcel('appointmentsTable')"
      class="bg-green-600 hover:bg-green-700 text-white px-4 py-2 rounded">
      Download Excel
    </button>

    <a
      href="/appointments/create"
      class="bg-indigo-600 hover:bg-indigo-700 text-white px-4 py-2 rounded"
      >New Appointment</a>
  </div>
</div>

<c:if test="${not empty error}">
  <div class="bg-red-100 text-red-700 p-3 rounded mb-4">${error}</div>
</c:if>

<!-- IMPORTANT: Add id="appointmentsTable" -->
<table id="appointmentsTable" class="min-w-full border border-gray-200 rounded">
  <thead class="bg-gray-100">
    <tr>
      <th class="px-4 py-2 border">ID</th>
      <th class="px-4 py-2 border">Patient</th>
      <th class="px-4 py-2 border">Doctor</th>
      <th class="px-4 py-2 border">Date</th>
      <th class="px-4 py-2 border">Time</th>
      <th class="px-4 py-2 border">Status</th>
      <th class="px-4 py-2 border">Remarks</th>
      <th class="px-4 py-2 border">Actions</th>
    </tr>
  </thead>
  <tbody>
    <c:forEach var="appointment" items="${appointments}">
      <tr class="hover:bg-gray-50">
        <td class="px-4 py-2 border">${appointment.appointmentId}</td>

        <td class="px-4 py-2 border">
          <c:forEach var="p" items="${patients}">
            <c:if test="${p.id eq appointment.patientId}">${p.name}</c:if>
          </c:forEach>
        </td>

        <td class="px-4 py-2 border">
          <c:forEach var="p" items="${doctors}">
            <c:if test="${p.id eq appointment.doctorId}">${p.name}</c:if>
          </c:forEach>
        </td>

        <td class="px-4 py-2 border">${appointment.date}</td>
        <td class="px-4 py-2 border">${appointment.time}</td>
        <td class="px-4 py-2 border">${appointment.status}</td>
        <td class="px-4 py-2 border">${appointment.remarks}</td>

        <td class="px-4 py-2 border space-x-2">
          <a href="/appointments/edit/${appointment.id}" class="text-blue-600 hover:underline">Edit</a>
          <a href="/appointments/delete/${appointment.id}" class="text-red-600 hover:underline">Delete</a>

          <c:if test="${appointment.status != 'Annulé'}">
            <a href="/appointments/cancel/${appointment.id}" class="text-yellow-600 hover:underline">Cancel</a>
            <a href="/appointments/complete/${appointment.id}" class="text-green-600 hover:underline">Complete</a>
          </c:if>
        </td>
      </tr>
    </c:forEach>
  </tbody>
</table>

<!-- EXPORT TO EXCEL SCRIPT -->
<script>
function exportTableToExcel(tableID, filename = '') {
    let dataType = 'application/vnd.ms-excel';
    let tableSelect = document.getElementById(tableID);
    let tableHTML = tableSelect.outerHTML.replace(/ /g, '%20');

    filename = filename ? filename + '.xls' : 'appointments.xls';

    let downloadLink = document.createElement("a");
    document.body.appendChild(downloadLink);

    if (navigator.msSaveOrOpenBlob) {
        let blob = new Blob(['\ufeff', tableHTML], { type: dataType });
        navigator.msSaveOrOpenBlob(blob, filename);
    } else {
        downloadLink.href = 'data:' + dataType + ', ' + tableHTML;
        downloadLink.download = filename;
        downloadLink.click();
    }
}
</script>
