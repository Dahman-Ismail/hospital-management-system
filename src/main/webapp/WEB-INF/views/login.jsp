<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div class="bg-gray-100 min-h-screen flex items-center justify-center">

  <div class="bg-white shadow-lg rounded-lg p-8 w-full max-w-md">
    <h2 class="text-2xl font-bold text-gray-800 mb-6 text-center">Login</h2>

    <form action="/login" method="post" class="space-y-4">
      <div>
        <label class="block text-gray-700 font-medium mb-1">Email</label>
        <input 
          type="email" 
          name="email" 
          required 
          class="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500"
        />
      </div>

      <div>
        <label class="block text-gray-700 font-medium mb-1">Password</label>
        <input 
          type="password" 
          name="password" 
          required 
          class="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500"
        />
      </div>

      <div>
        <button 
          type="submit" 
          class="w-full bg-indigo-600 text-white font-semibold py-2 px-4 rounded-lg hover:bg-indigo-500 transition"
        >
          Login
        </button>
      </div>
    </form>
  </div>
</div>