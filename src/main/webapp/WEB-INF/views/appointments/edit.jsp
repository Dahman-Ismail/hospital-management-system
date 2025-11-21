<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<h2 class="text-2xl font-bold mb-4">Edit Appointment</h2>

<c:if test="${not empty error}">
    <div class="bg-red-100 text-red-700 p-3 rounded mb-4">${error}</div>
</c:if>

<form action="/appointments/edit/${appointment.id}" method="post" class="space-y-4 max-w-lg">
    <div>
        <label class="block mb-1 font-medium">Patient</label>
        <select name="patientId" class="w-full border border-gray-300 rounded px-3 py-2">
            <option value="">Select Patient</option>
            <c:forEach var="patient" items="${patients}">
                <option value="${patient.id}" <c:if test="${appointment.patientId == patient.id}">selected</c:if>>${patient.name}</option>
            </c:forEach>
        </select>
    </div>

    <div>
        <label class="block mb-1 font-medium">Doctor</label>
        <select name="doctorId" class="w-full border border-gray-300 rounded px-3 py-2">
            <option value="">Select Doctor</option>
            <c:forEach var="doctor" items="${doctors}">
                <option value="${doctor.id}" <c:if test="${appointment.doctorId == doctor.id}">selected</c:if>>${doctor.name}</option>
            </c:forEach>
        </select>
    </div>

    <div>
        <label class="block mb-1 font-medium">Date</label>
        <input type="date" name="date" value="${appointment.date}" class="w-full border border-gray-300 rounded px-3 py-2">
    </div>

    <div>
        <label class="block mb-1 font-medium">Time</label>
        <input type="time" name="time" value="${appointment.time}" class="w-full border border-gray-300 rounded px-3 py-2">
    </div>

    <div>
        <label class="block mb-1 font-medium">Status</label>
        <select name="status" class="w-full border border-gray-300 rounded px-3 py-2">
            <option value="Planifié" <c:if test="${appointment.status == 'Planifié'}">selected</c:if>>Planifié</option>
            <option value="Terminé" <c:if test="${appointment.status == 'Terminé'}">selected</c:if>>Terminé</option>
            <option value="Annulé" <c:if test="${appointment.status == 'Annulé'}">selected</c:if>>Annulé</option>
        </select>
    </div>

    <div>
        <label class="block mb-1 font-medium">Remarks</label>
        <textarea name="remarks" class="w-full border border-gray-300 rounded px-3 py-2">${appointment.remarks}</textarea>
    </div>

    <div class="space-x-2">
        <button type="submit" class="bg-indigo-600 hover:bg-indigo-700 text-white px-4 py-2 rounded">Update</button>
        <a href="/appointments" class="bg-gray-300 hover:bg-gray-400 text-gray-800 px-4 py-2 rounded">Cancel</a>
    </div>
</form>
