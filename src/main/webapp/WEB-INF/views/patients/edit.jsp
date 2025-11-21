<div class="max-w-xl mx-auto bg-white shadow-md rounded-lg p-6">
    <h2 class="text-2xl font-bold mb-4">Edit Patient</h2>

    <form method="post" action="/patients/edit/${patient.id}" class="space-y-4">
        <div>
            <label class="block text-gray-700">Patient ID</label>
            <input type="text" name="patientId" value="${patient.patientId}" required
                   class="w-full border border-gray-300 rounded px-3 py-2 focus:outline-none focus:ring-2 focus:ring-indigo-500">
        </div>
        <div>
            <label class="block text-gray-700">Name</label>
            <input type="text" name="name" value="${patient.name}" required
                   class="w-full border border-gray-300 rounded px-3 py-2 focus:outline-none focus:ring-2 focus:ring-indigo-500">
        </div>
        <div>
            <label class="block text-gray-700">Date of Birth</label>
            <input type="date" name="dob" value="${patient.dob}" required
                   class="w-full border border-gray-300 rounded px-3 py-2 focus:outline-none focus:ring-2 focus:ring-indigo-500">
        </div>
        <div>
            <label class="block text-gray-700">Gender</label>
            <select name="gender" required
                    class="w-full border border-gray-300 rounded px-3 py-2 focus:outline-none focus:ring-2 focus:ring-indigo-500">
                <option value="Male" ${patient.gender == 'Male' ? 'selected' : ''}>Male</option>
                <option value="Female" ${patient.gender == 'Female' ? 'selected' : ''}>Female</option>
            </select>
        </div>
        <div>
            <label class="block text-gray-700">Phone</label>
            <input type="text" name="phone" value="${patient.phone}" required
                   class="w-full border border-gray-300 rounded px-3 py-2 focus:outline-none focus:ring-2 focus:ring-indigo-500">
        </div>
        <div>
            <label class="block text-gray-700">Email</label>
            <input type="email" name="email" value="${patient.email}"
                   class="w-full border border-gray-300 rounded px-3 py-2 focus:outline-none focus:ring-2 focus:ring-indigo-500">
        </div>
        <div>
            <label class="block text-gray-700">Address</label>
            <textarea name="address" rows="3" 
                      class="w-full border border-gray-300 rounded px-3 py-2 focus:outline-none focus:ring-2 focus:ring-indigo-500">${patient.address}</textarea>
        </div>

        <div class="flex justify-end gap-2">
            <a href="/patients" class="px-4 py-2 bg-gray-300 rounded hover:bg-gray-400">Cancel</a>
            <button type="submit" class="px-4 py-2 bg-indigo-600 text-white rounded hover:bg-indigo-500">Update</button>
        </div>
    </form>
</div>
