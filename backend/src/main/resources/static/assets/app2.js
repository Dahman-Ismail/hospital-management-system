// ========================
// CONFIG
// ========================
const API_BASE = "http://localhost:8080/api"; // <== ADAPT IF NEEDED

async function api(path, method = "GET", body = null) {
  const opts = {
    method,
    headers: { "Content-Type": "application/json" }
  };
  if (body) opts.body = JSON.stringify(body);

  const res = await fetch(API_BASE + path, opts);

  if (!res.ok) {
    const text = await res.text();
    let err;
    try {
      err = JSON.parse(text);
    } catch (e) {
      err = { message: text };
    }
    throw err;
  }
  return res.status === 204 ? null : res.json();
}

// ========================
// DASHBOARD
// ========================
export async function initDashboard() {
  try {
    const patients = await api("/patients");
    const doctors = await api("/doctors");
    const allAppointments = await api("/appointments").catch(() => []);

    // Counts
    document.getElementById("patientCount").textContent = patients.length;
    document.getElementById("doctorCount").textContent = doctors.length;

    // Today's appointments
    const todayKey = new Date().toISOString().slice(0, 10);
    const todayList = allAppointments.filter(
      (a) => a.date && a.date.startsWith(todayKey)
    );

    document.getElementById("todayCount").textContent = todayList.length;
    document.getElementById("todayListPreview").textContent = todayList
      .slice(0, 3)
      .map((a) => `${a.patientSnapshot?.firstName ?? "Patient"} - ${a.date}`)
      .join(" • ");

    // RDV per doctor
    const byDoctor = {};
    allAppointments.forEach((a) => {
      if (a.doctorId) byDoctor[a.doctorId] = (byDoctor[a.doctorId] || 0) + 1;
    });

    const doctorMap = {};
    doctors.forEach((d) => {
      doctorMap[d.id] = `${d.firstName} ${d.lastName}`;
    });

    renderBarChart(
      "chartByDoctor",
      Object.keys(byDoctor).map((id) => doctorMap[id] || id),
      Object.values(byDoctor),
      "RDV par médecin"
    );

    // RDV per day (7 days)
    const days = {};
    for (let i = 6; i >= 0; i--) {
      const d = new Date();
      d.setDate(d.getDate() - i);
      const k = d.toISOString().slice(0, 10);
      days[k] = 0;
    }

    allAppointments.forEach((a) => {
      const k = a.date?.slice(0, 10);
      if (k && days[k] !== undefined) days[k]++;
    });

    renderLineChart(
      "chartByDay",
      Object.keys(days),
      Object.values(days),
      "RDV / jour (7j)"
    );

    // Top patients
    const visits = {};
    allAppointments.forEach((a) => {
      const key =
        a.patientId ||
        (a.patientSnapshot &&
          `${a.patientSnapshot.firstName} ${a.patientSnapshot.lastName}`) ||
        "inconnu";
      visits[key] = (visits[key] || 0) + 1;
    });

    const top = Object.entries(visits)
      .sort((a, b) => b[1] - a[1])
      .slice(0, 5);

    const ul = document.getElementById("topPatientsList");
    ul.innerHTML = "";
    top.forEach(([pid, n]) => {
      const li = document.createElement("li");
      li.textContent = `${pid} — ${n} visite(s)`;
      ul.appendChild(li);
    });
  } catch (err) {
    console.error(err);
    alert("Erreur dashboard : " + (err.message || JSON.stringify(err)));
  }
}

// ========================
// PATIENTS
// ========================
export function initPatients() {
  const form = document.getElementById("patientForm");
  const table = document.getElementById("patientsTable");
  const search = document.getElementById("searchPatient");

  async function load() {
    const list = await api("/patients");
    render(list);
  }

  function render(list) {
    table.innerHTML = "";
    list.forEach((p) => {
      const tr = document.createElement("tr");
      tr.innerHTML = `
        <td class="py-2">${p.firstName} ${p.lastName}</td>
        <td>${p.phone || ""}</td>
        <td>${p.email || ""}</td>
        <td>
          <button class="edit text-indigo-600 mr-3" data-id="${p.id}">Edit</button>
          <button class="delete text-red-600" data-id="${p.id}">Delete</button>
        </td>
      `;
      table.appendChild(tr);
    });

    // Edit
    table.querySelectorAll(".edit").forEach((btn) => {
      btn.onclick = async () => {
        const p = await api(`/patients/${btn.dataset.id}`);
        document.getElementById("patientId").value = p.id;
        document.getElementById("firstName").value = p.firstName;
        document.getElementById("lastName").value = p.lastName;
        document.getElementById("birthDate").value = p.birthDate;
        document.getElementById("phone").value = p.phone;
        document.getElementById("email").value = p.email;
      };
    });

    // Delete
    table.querySelectorAll(".delete").forEach((btn) => {
      btn.onclick = async () => {
        if (!confirm("Supprimer ce patient ?")) return;
        await api(`/patients/${btn.dataset.id}`, "DELETE");
        load();
      };
    });
  }

  form.onsubmit = async (ev) => {
    ev.preventDefault();
    const id = document.getElementById("patientId").value;

    const payload = {
      firstName: document.getElementById("firstName").value,
      lastName: document.getElementById("lastName").value,
      birthDate: document.getElementById("birthDate").value,
      phone: document.getElementById("phone").value,
      email: document.getElementById("email").value
    };

    if (id) await api(`/patients/${id}`, "PUT", payload);
    else await api("/patients", "POST", payload);

    form.reset();
    document.getElementById("patientId").value = "";
    load();
  };

  search.oninput = async () => {
    const q = search.value.toLowerCase();
    const list = await api("/patients");
    render(
      list.filter((p) =>
        `${p.firstName} ${p.lastName}`.toLowerCase().includes(q)
      )
    );
  };

  load();
}

// ========================
// DOCTORS
// ========================
export function initDoctors() {
  const form = document.getElementById("doctorForm");
  const table = document.getElementById("doctorsTable");

  async function load() {
    const list = await api("/doctors");
    table.innerHTML = "";
    list.forEach((d) => {
      const tr = document.createElement("tr");
      tr.innerHTML = `
        <td class="py-2">${d.firstName} ${d.lastName}</td>
        <td>${d.specialty || ""}</td>
        <td>
          <button class="edit text-indigo-600 mr-3" data-id="${d.id}">Edit</button>
          <button class="delete text-red-600" data-id="${d.id}">Delete</button>
        </td>
      `;
      table.appendChild(tr);
    });

    table.querySelectorAll(".edit").forEach((btn) => {
      btn.onclick = async () => {
        const doc = await api(`/doctors/${btn.dataset.id}`);
        document.getElementById("doctorId").value = doc.id;
        document.getElementById("docFirstName").value = doc.firstName;
        document.getElementById("docLastName").value = doc.lastName;
        document.getElementById("specialty").value = doc.specialty;
      };
    });

    table.querySelectorAll(".delete").forEach((btn) => {
      btn.onclick = async () => {
        if (!confirm("Supprimer ce médecin ?")) return;
        await api(`/doctors/${btn.dataset.id}`, "DELETE");
        load();
      };
    });
  }

  form.onsubmit = async (ev) => {
    ev.preventDefault();
    const id = document.getElementById("doctorId").value;

    const payload = {
      firstName: document.getElementById("docFirstName").value,
      lastName: document.getElementById("docLastName").value,
      specialty: document.getElementById("specialty").value
    };

    if (id) await api(`/doctors/${id}`, "PUT", payload);
    else await api("/doctors", "POST", payload);

    form.reset();
    document.getElementById("doctorId").value = "";
    load();
  };

  load();
}

// ========================
// APPOINTMENTS
// ========================
export function initAppointments() {
  const form = document.getElementById("appointmentForm");
  const table = document.getElementById("appointmentsTable");
  const patientSelect = document.getElementById("patientSelect");
  const doctorSelect = document.getElementById("doctorSelect");

  async function load() {
    const [patients, doctors, appointments] = await Promise.all([
      api("/patients"),
      api("/doctors"),
      api("/appointments").catch(() => [])
    ]);

    // Fill selects
    patientSelect.innerHTML = "";
    doctorSelect.innerHTML = "";
    patients.forEach((p) => {
      const opt = document.createElement("option");
      opt.value = p.id;
      opt.textContent = `${p.firstName} ${p.lastName}`;
      patientSelect.appendChild(opt);
    });
    doctors.forEach((d) => {
      const opt = document.createElement("option");
      opt.value = d.id;
      opt.textContent = `${d.firstName} ${d.lastName} (${d.specialty || ""})`;
      doctorSelect.appendChild(opt);
    });

    // Table
    table.innerHTML = "";
    appointments.forEach((a) => {
      const tr = document.createElement("tr");
      const doctor = doctors.find((d) => d.id === a.doctorId);
      const patient = patients.find((p) => p.id === a.patientId);

      tr.innerHTML = `
        <td class="py-2">${a.date?.replace("T", " ").replace("Z", "")}</td>
        <td>${doctor ? doctor.firstName + " " + doctor.lastName : "—"}</td>
        <td>${a.patientSnapshot?.firstName ?? patient?.firstName ?? ""} ${
        a.patientSnapshot?.lastName ?? patient?.lastName ?? ""
      }</td>
        <td>${a.status || "—"}</td>
        <td>
          <button class="edit text-indigo-600 mr-3" data-id="${a.id}">Edit</button>
          <button class="delete text-red-600" data-id="${a.id}">Delete</button>
        </td>
      `;
      table.appendChild(tr);
    });

    table.querySelectorAll(".edit").forEach((btn) => {
      btn.onclick = async () => {
        const a = await api(`/appointments/${btn.dataset.id}`);
        document.getElementById("appointmentId").value = a.id;
        patientSelect.value = a.patientId;
        doctorSelect.value = a.doctorId;

        if (a.date) {
          const dt = new Date(a.date);
          const localISO = new Date(
            dt.getTime() - dt.getTimezoneOffset() * 60000
          )
            .toISOString()
            .slice(0, 16);
          document.getElementById("dateTime").value = localISO;
        }
        document.getElementById("duration").value = a.durationMinutes ?? 30;
      };
    });

    table.querySelectorAll(".delete").forEach((btn) => {
      btn.onclick = async () => {
        if (!confirm("Supprimer ce rendez-vous ?")) return;
        await api(`/appointments/${btn.dataset.id}`, "DELETE");
        load();
      };
    });
  }

  form.onsubmit = async (ev) => {
    ev.preventDefault();
    const id = document.getElementById("appointmentId").value;

    const dt = document.getElementById("dateTime").value;
    const iso = dt ? new Date(dt).toISOString() : null;

    const payload = {
      patientId: patientSelect.value,
      doctorId: doctorSelect.value,
      date: iso,
      durationMinutes: parseInt(
        document.getElementById("duration").value,
        10
      )
    };

    if (id) await api(`/appointments/${id}`, "PUT", payload);
    else await api("/appointments", "POST", payload);

    form.reset();
    document.getElementById("appointmentId").value = "";
    load();
  };

  load();
}

// ========================
// CHART HELPERS
// ========================
let charts = {};

export function renderBarChart(id, labels, data, label) {
  const ctx = document.getElementById(id).getContext("2d");
  if (charts[id]) charts[id].destroy();
  charts[id] = new Chart(ctx, {
    type: "bar",
    data: { labels, datasets: [{ label, data }] },
    options: { responsive: true, maintainAspectRatio: false }
  });
}

export function renderLineChart(id, labels, data, label) {
  const ctx = document.getElementById(id).getContext("2d");
  if (charts[id]) charts[id].destroy();
  charts[id] = new Chart(ctx, {
    type: "line",
    data: { labels, datasets: [{ label, data, fill: false }] },
    options: { responsive: true, maintainAspectRatio: false }
  });
}
