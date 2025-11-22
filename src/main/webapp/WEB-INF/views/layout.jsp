<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <title>${pageTitle}</title>
  <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gray-50 min-h-screen flex flex-col">

  <!-- Header -->
  <header class="bg-gradient-to-r from-indigo-600 to-purple-600 text-white shadow-md">
    <div class="container mx-auto flex justify-between items-center py-4 px-6">
      <h1 class="text-2xl font-extrabold tracking-wide">
        <a href="/" class="hover:text-indigo-200 transition">Hospital Management</a>
      </h1>
      <nav class="flex items-center gap-4">
        <c:if test="${not empty sessionScope.user}">
          <!-- Role Badge -->
          <span class="px-4 py-2 rounded-full bg-indigo-500 text-white font-semibold shadow-md">
            ${sessionScope.user.role}
          </span>

          <!-- Logout Button -->
          <a href="/logout" 
             class="px-4 py-2 rounded-lg bg-red-500 hover:bg-red-600 transition font-medium shadow-md">
            Logout
          </a>
        </c:if>

        <c:if test="${empty sessionScope.user}">
          <a href="/login" 
             class="px-4 py-2 rounded-lg bg-indigo-500 hover:bg-indigo-400 transition font-medium shadow-md">
            Login
          </a>
        </c:if>
      </nav>
    </div>
  </header>

  <!-- Main Content -->
  <main class="flex-1 container mx-auto px-6 py-8">
    <c:if test="${not empty error}">
      <div class="bg-red-100 border-l-4 border-red-500 text-red-700 p-4 rounded-lg mb-6 shadow-sm">
        ${error}
      </div>
    </c:if>

    <jsp:include page="${contentPage}" />
  </main>

  <!-- Footer -->
  <footer class="bg-gray-100 text-gray-700 py-6 mt-12 shadow-inner">
    <div class="container mx-auto text-center text-sm">
      &copy; 2025 <span class="font-semibold">Hospital Management</span>. All rights reserved.
    </div>
  </footer>

</body>
</html>
