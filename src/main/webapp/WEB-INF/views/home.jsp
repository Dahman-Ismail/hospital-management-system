<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="mb-6 border-b pb-4">
  <h1 class="text-3xl font-bold text-gray-800">Dashboard</h1>
</div>

<!-- Top Cards -->

<div class="grid grid-cols-1 md:grid-cols-3 gap-6">
  <!-- Only show Patients and Doctors cards to ADMIN -->
  <c:if test="${sessionScope.user.role == 'ADMIN'}">
    <!-- Patients Card -->
    <div
      class="bg-blue-600 text-white rounded-lg shadow-md p-6 flex flex-col justify-between hover:scale-105 transform transition"
    >
      <div>
        <h2 class="text-xl font-semibold mb-2">Patients</h2>
        <p class="text-blue-100">Manage patient records</p>
      </div>
      <a
        href="/patients"
        class="mt-4 inline-block bg-white text-blue-600 font-semibold px-4 py-2 rounded hover:bg-gray-100 transition"
      >
        Go to Patients
      </a>
    </div>

    <!-- Doctors Card -->
    <div
      class="bg-green-600 text-white rounded-lg shadow-md p-6 flex flex-col justify-between hover:scale-105 transform transition"
    >
      <div>
        <h2 class="text-xl font-semibold mb-2">Doctors</h2>
        <p class="text-green-100">Manage doctor information</p>
      </div>
      <a
        href="/doctors"
        class="mt-4 inline-block bg-white text-green-600 font-semibold px-4 py-2 rounded hover:bg-gray-100 transition"
      >
        Go to Doctors
      </a>
    </div>
  </c:if>

  <!-- Appointments Card: visible to everyone -->
  <div
    class="bg-yellow-500 text-white rounded-lg shadow-md p-6 flex flex-col justify-between hover:scale-105 transform transition"
  >
    <div>
      <h2 class="text-xl font-semibold mb-2">Appointments</h2>
      <p class="text-yellow-100">Schedule and manage appointments</p>
    </div>
    <a
      href="/appointments"
      class="mt-4 inline-block bg-white text-yellow-500 font-semibold px-4 py-2 rounded hover:bg-gray-100 transition"
    >
      Go to Appointments
    </a>
  </div>
</div>

<!-- Quick Actions -->
<div class="mt-8 bg-white rounded-lg shadow-md p-6">
  <h3 class="text-2xl font-bold mb-4">Quick Actions</h3>
  <div class="flex flex-wrap gap-4">
    <!-- Admin can add patients and doctors -->
    <c:if test="${sessionScope.user.role == 'ADMIN'}">
      <a
        href="/patients/create"
        class="px-4 py-2 border border-blue-600 text-blue-600 rounded hover:bg-blue-50 transition"
        >Add New Patient</a
      >

      <a
        href="/doctors/create"
        class="px-4 py-2 border border-green-600 text-green-600 rounded hover:bg-green-50 transition"
        >Add New Doctor</a
      >
    </c:if>

    <!-- Everyone can schedule appointments -->
    <a
      href="/appointments/create"
      class="px-4 py-2 border border-yellow-500 text-yellow-500 rounded hover:bg-yellow-50 transition"
      >Schedule Appointment</a
    >
  </div>
</div>
