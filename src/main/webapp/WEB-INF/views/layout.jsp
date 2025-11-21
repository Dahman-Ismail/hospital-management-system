<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <title>${pageTitle}</title>
    <script src="https://cdn.tailwindcss.com"></script>
  </head>
  <body class="bg-gray-100 min-h-screen flex flex-col">
    <!-- Header -->
    <header class="bg-indigo-600 text-white p-4 shadow-md">
      <div class="container mx-auto flex justify-between items-center">
        <h1 class="text-2xl font-bold">
          <a href="/" class="hover:text-indigo-200">Hospital Management</a>
        </h1>
        <nav>
          <a href="/patients" class="px-3 py-2 rounded hover:bg-indigo-500"
            >Patients</a
          >
          <!-- Add other links here -->
        </nav>
      </div>
    </header>

    <!-- Main content -->
    <main class="flex-1 container mx-auto p-4">
      <c:if test="${not empty error}">
        <div class="bg-red-100 text-red-700 p-3 rounded mb-4">${error}</div>
      </c:if>
      <jsp:include page="${contentPage}" />
    </main>

    <!-- Footer -->
    <footer class="bg-gray-200 text-gray-700 p-4 text-center mt-8">
      &copy; 2025 Hospital Management
    </footer>
  </body>
</html>
