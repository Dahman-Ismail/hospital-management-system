// assets/app.js
const API_BASE = '/api'; // adapte si ton backend est sur autre URL (ex: http://localhost:8080/api)

async function api(path, method='GET', body=null) {
  const opts = { method, headers: { 'Content-Type':'application/json' } };
  if (body) opts.body = JSON.stringify(body);
  const res = await fetch(API_BASE + path, opts);
  if (!res.ok) {
    const text = await res.text();
    let err;
    try { err = JSON.parse(text); } catch(e) { err = { message: text }; }
    throw err;
  }
  return res.status === 204 ? null : res.json();
}

/* ---------------- DASHBOARD ---------------- */
export async function initDashboard(){
  try {
    // counts
    const patients = await api('/patients');
    const doctors = await api('/doctors');

    document.getElementById('patientCount').textContent = patients.length;
    document.getElementById('doctorCount').textContent = doctors.length;

    // today's appointments
    const today = new Date().toISOString().slice(0,10); // YYYY-MM-DD
    const appointmentsToday = await api(`/appointments/doctor/${doctors.length?doctors[0].id: ''}/day?day=${today}`)
      .catch(()=>[]); // if no doctor, fallback

    // fallback: fetch all and compute
    let allAppointments = [];
    try { allAppointments = await api('/appointments'); } catch(e) { allAppointments = []; }

    const nowDay = new Date().toISOString().slice(0,10);
    const todayList = allAppointments.filter(a => a.date && a.date.startsWith(nowDay));
    document.getElementById('todayCount').textContent = todayList.length;
    document.getElementById('todayListPreview').textContent = todayList.slice(0,3).map(a => `${a.patientSnapshot?.firstName??'Patient'} - ${a.date}`).join(' • ');

    // stats: appointments per doctor
    const byDoctor = {};
    allAppointments.forEach(a => {
      if (!a.doctorId) return;
      byDoctor[a.doctorId] = (byDoctor[a.doctorId] || 0) + 1;
    });

    // map doctorId->name
    const doctorMap = {};
    doctors.forEach(d => doctorMap[d.id] = `${d.firstName} ${d.lastName}`);
    const labels1 = Object.keys(byDoctor).map(id => doctorMap[id] ?? id);
    const data1 = Object.values(byDoctor);

    renderBarChart('chartByDoctor', labels1, data1, 'RDV par médecin');

    // appointments by day (7 days)
    const countsByDay = {};
    for (let i=6;i>=0;i--){
      const d = new Date(); d.setDate(d.getDate()-i);
      const key = d.toISOString().slice(0,10);
      countsByDay[key] = 0;
    }
    allAppointments.forEach(a => {
      const day = a.date ? a.date.slice(0,10) : null;
      if (day && countsByDay.hasOwnProperty(day)) countsByDay[day]++;
    });
    renderLineChart('chartByDay', Object.keys(countsByDay), Object.values(countsByDay), 'RDV / jour (7j)');

    // top patients
    const patientVisits = {};
    allAppointments.forEach(a => {
      const pid = a.patientId || (a.patientSnapshot && (a.patientSnapshot.firstName+' '+a.patientSnapshot.lastName)) || 'inconnu';
      patientVisits[pid] = (patientVisits[pid]||0) + 1;
    });
    const top = Object.entries(patientVisits).sort((a,b)=>b[1]-a[1]).slice(0,5);
    const topList = document.getElementById('topPatientsList');
    topList.innerHTML = '';
    top.forEach(([pid,c])=>{
      const li = document.createElement('li');
      li.textContent = `${pid} — ${c} visite(s)`;
      topList.appendChild(li);
    });

  } catch (err) {
    console.error('Dashboard error', err);
    alert('Erreur dashboard: ' + (err.message || JSON.stringify(err)));
  }
}

/* ---------------- PATIENTS ---------------- */
export function initPatients(){
  const form = document.getElementById('patientForm');
  const table = document.getElementById('patientsTable');
  const search = document.getElementById('searchPatient');

  async function load(){
    const list = await api('/patients');
    renderTable(list);
  }

  function renderTable(list){
    table.innerHTML = '';
    list.forEach(p => {
      const tr = document.createElement('tr');
      tr.innerHTML = `
        <td class="py-2">${p.firstName} ${p.lastName}</td>
        <td>${p.phone ?? ''}</td>
        <td>${p.email ?? ''}</td>
        <td>
          <button data-id="${p.id}" class="edit text-indigo-600 mr-3">Edit</button>
          <button data-id="${p.id}" class="delete text-red-600">Delete</button>
        </td>
      `;
      table.appendChild(tr);
    });

    table.querySelectorAll('.edit').forEach(btn=>{
      btn.addEventListener('click', async e=>{
        const id = e.target.dataset.id;
        const p = await api('/patients/' + id);
        document.getElementById('patientId').value = p.id;
        document.getElementById('firstName').value = p.firstName || '';
        document.getElementById('lastName').value = p.lastName || '';
        document.getElementById('birthDate').value = p.birthDate || '';
        document.getElementById('phone').value = p.phone || '';
        document.getElementById('email').value = p.email || '';
      });
    });

    table.querySelectorAll('.delete').forEach(btn=>{
      btn.addEventListener('click', async e=>{
        if (!confirm('Supprimer ce patient ?')) return;
        const id = e.target.dataset.id;
        await api('/patients/' + id, 'DELETE');
        load();
      });
    });
  }

  form.addEventListener('submit', async (ev)=>{
    ev.preventDefault();
    const id = document.getElementById('patientId').value;
    const payload = {
      patientId: id ? undefined : undefined,
      firstName: document.getElementById('firstName').value,
      lastName: document.getElementById('lastName').value,
      birthDate: document.getElementById('birthDate').value,
      phone: document.getElementById('phone').value,
      email: document.getElementById('email').value
    };
    if (id) {
      await api('/patients/' + id, 'PUT', payload);
    } else {
      await api('/patients', 'POST', payload);
    }
    form.reset();
    load();
  });

  document.getElementById('resetPatient').addEventListener('click', ()=>{ form.reset(); document.getElementById('patientId').value=''; });

  search.addEventListener('input', async ()=>{
    const q = search.value.toLowerCase();
    const list = await api('/patients');
    const filtered = list.filter(p => (p.firstName + ' ' + p.lastName).toLowerCase().includes(q));
    renderTable(filtered);
  });

  // initial load
  load();
}

/* ---------------- DOCTORS ---------------- */
export function initDoctors(){
  const form = document.getElementById('doctorForm');
  const table = document.getElementById('doctorsTable');

  async function load(){
    const list = await api('/doctors');
    table.innerHTML = '';
    list.forEach(d=>{
      const tr = document.createElement('tr');
      tr.innerHTML = `<td class="py-2">${d.firstName} ${d.lastName}</td><td>${d.specialty || ''}</td><td><button data-id="${d.id}" class="edit text-indigo-600 mr-3">Edit</button><button data-id="${d.id}" class="delete text-red-600">Delete</button></td>`;
      table.appendChild(tr);
    });

    table.querySelectorAll('.edit').forEach(btn=>{
      btn.addEventListener('click', async e=>{
        const id = e.target.dataset.id;
        const doc = await api('/doctors/' + id);
        document.getElementById('doctorId').value = doc.id;
        document.getElementById('docFirstName').value = doc.firstName || '';
        document.getElementById('docLastName').value = doc.lastName || '';
        document.getElementById('specialty').value = doc.specialty || '';
      });
    });

    table.querySelectorAll('.delete').forEach(btn=>{
      btn.addEventListener('click', async e=>{
        if (!confirm('Supprimer ce médecin ?')) return;
        const id = e.target.dataset.id;
        await api('/doctors/' + id, 'DELETE');
        load();
      });
    });
  }

  form.addEventListener('submit', async (ev)=>{
    ev.preventDefault();
    const id = document.getElementById('doctorId').value;
    const payload = {
      doctorId: id ? undefined : undefined,
      firstName: document.getElementById('docFirstName').value,
      lastName: document.getElementById('docLastName').value,
      specialty: document.getElementById('specialty').value
    };
    if (id) await api('/doctors/' + id, 'PUT', payload);
    else await api('/doctors', 'POST', payload);
    form.reset();
    load();
  });

  document.getElementById('resetDoctor').addEventListener('click', ()=>{ form.reset(); document.getElementById('doctorId').value=''; });

  load();
}

/* ---------------- APPOINTMENTS ---------------- */
export function initAppointments(){
  const form = document.getElementById('appointmentForm');
  const table = document.getElementById('appointmentsTable');
  const patientSelect = document.getElementById('patientSelect');
  const doctorSelect = document.getElementById('doctorSelect');

  async function load(){
    const [patients, doctors, appointments] = await Promise.all([
      api('/patients'), api('/doctors'), api('/appointments').catch(()=>[])
    ]);

    // fill selects
    patientSelect.innerHTML = ''; doctorSelect.innerHTML = '';
    patients.forEach(p => {
      const opt = document.createElement('option');
      opt.value = p.id; opt.textContent = `${p.firstName} ${p.lastName}`;
      patientSelect.appendChild(opt);
    });
    doctors.forEach(d => {
      const opt = document.createElement('option');
      opt.value = d.id; opt.textContent = `${d.firstName} ${d.lastName} (${d.specialty||''})`;
      doctorSelect.appendChild(opt);
    });

    // render appointments table
    table.innerHTML = '';
    appointments.forEach(a=>{
      const tr = document.createElement('tr');
      tr.innerHTML = `<td class="py-2">${a.date?.replace('T',' ').replace('Z','') || ''}</td>
        <td>${ (doctors.find(d=>d.id===a.doctorId)?.firstName ?? 'Médecin') } ${doctors.find(d=>d.id===a.doctorId)?.lastName ?? '' }</td>
        <td>${ a.patientSnapshot?.firstName ?? (patients.find(p=>p.id===a.patientId)?.firstName ?? 'Patient') } ${ a.patientSnapshot?.lastName ?? (patients.find(p=>p.id===a.patientId)?.lastName ?? '') }</td>
        <td>${a.status}</td>
        <td>
          <button data-id="${a.id}" class="edit text-indigo-600 mr-3">Edit</button>
          <button data-id="${a.id}" class="delete text-red-600">Delete</button>
        </td>`;
      table.appendChild(tr);
    });

    // edit/delete buttons
    table.querySelectorAll('.edit').forEach(btn=>{
      btn.addEventListener('click', async e=>{
        const id = e.target.dataset.id;
        const a = await api('/appointments/' + id);
        document.getElementById('appointmentId').value = a.id;
        document.getElementById('patientSelect').value = a.patientId || '';
        document.getElementById('doctorSelect').value = a.doctorId || '';
        // convert ISO to datetime-local: remove Z and adjust
        if (a.date) {
          const dt = new Date(a.date);
          const localISO = new Date(dt.getTime() - dt.getTimezoneOffset()*60000).toISOString().slice(0,16);
          document.getElementById('dateTime').value = localISO;
        }
        document.getElementById('duration').value = a.durationMinutes || 30;
      });
    });

    table.querySelectorAll('.delete').forEach(btn=>{
      btn.addEventListener('click', async e=>{
        if (!confirm('Supprimer ce rendez-vous ?')) return;
        const id = e.target.dataset.id;
        await api('/appointments/' + id, 'DELETE');
        load();
      });
    });
  }

  form.addEventListener('submit', async ev=>{
    ev.preventDefault();
    const id = document.getElementById('appointmentId').value;
    // convert datetime-local to ISOZ
    const dtLocal = document.getElementById('dateTime').value;
    const dateISO = dtLocal ? new Date(dtLocal).toISOString() : null;
    const payload = {
      patientId: document.getElementById('patientSelect').value,
      doctorId: document.getElementById('doctorSelect').value,
      date: dateISO,
      durationMinutes: parseInt(document.getElementById('duration').value,10)
    };
    try {
      if (id) await api('/appointments/' + id, 'PUT', payload);
      else await api('/appointments', 'POST', payload);
      form.reset();
      load();
    } catch (err) {
      console.error(err);
      alert('Erreur: ' + (err.message || JSON.stringify(err)));
    }
  });

  document.getElementById('resetAppointment').addEventListener('click', ()=>{ form.reset(); document.getElementById('appointmentId').value=''; });

  load();
}

/* ---------------- Charts helpers ---------------- */
let chartInstances = {};
export function renderBarChart(canvasId, labels, data, label){
  const ctx = document.getElementById(canvasId).getContext('2d');
  if (chartInstances[canvasId]) chartInstances[canvasId].destroy();
  chartInstances[canvasId] = new Chart(ctx, {
    type: 'bar',
    data: { labels, datasets: [{ label, data }] },
    options: { responsive: true, maintainAspectRatio: false }
  });
}
export function renderLineChart(canvasId, labels, data, label){
  const ctx = document.getElementById(canvasId).getContext('2d');
  if (chartInstances[canvasId]) chartInstances[canvasId].destroy();
  chartInstances[canvasId] = new Chart(ctx, {
    type: 'line',
    data: { labels, datasets: [{ label, data, fill:false }] },
    options: { responsive: true, maintainAspectRatio: false }
  });
}
