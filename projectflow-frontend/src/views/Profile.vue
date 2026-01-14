<template>
  <div class="profile-container">
    <h1 class="title">Profil Étudiant</h1>
    <p class="subtitle">Informations personnelles</p>

    <div class="profile-card">
      <div class="row">
        <span class="label">Nom complet</span>
        <span class="value">{{ profile.name }}</span>
      </div>

      <div class="row">
        <span class="label">Numéro étudiant</span>
        <span class="value">{{ profile.id }}</span>
      </div>

      <div class="row">
        <span class="label">Email</span>
        <span class="value">{{ profile.email }}</span>
      </div>

      <div class="row">
        <span class="label">Filière</span>
        <span class="value">{{ profile.field }}</span>
      </div>

      <div class="row">
        <span class="label">Année</span>
        <span class="value">{{ profile.year }}</span>
      </div>

      <div class="row">
        <span class="label">Encadrant</span>
        <span class="value">{{ profile.supervisor }}</span>
      </div>
    </div>

    <div class="actions">
      <button class="btn-primary">Modifier</button>
      
    </div>
  </div>
</template>

<script>
export default {
  name: "Profile",
  data() {
    return {
      profile: {
        name: "Amina Mourad",
        id: "ETU2024001",
        email: "amina.mourad@univ.ma",
        field: "Génie Informatique",
        year: "3ème année",
        supervisor: "Dr. Karim Benali"
      }
    }
  }
}
</script>

<style scoped>
.profile-container {
  padding: 30px;
  max-width: 800px;
  margin: auto;
}

.title {
  font-size: 2rem;
  color: #f97316;
  margin-bottom: 5px;
}

.subtitle {
  color: #6b7280;
  margin-bottom: 25px;
}

.profile-card {
  background: #ffffff;
  border-radius: 14px;
  padding: 25px;
  box-shadow: 0 10px 25px rgba(0,0,0,0.08);
}

.row {
  display: flex;
  justify-content: space-between;
  padding: 12px 0;
  border-bottom: 1px solid #e5e7eb;
}

.row:last-child {
  border-bottom: none;
}

.label {
  color: #6b7280;
  font-size: 0.9rem;
}

.value {
  font-weight: bold;
  color: #111827;
}

.actions {
  margin-top: 25px;
  display: flex;
  gap: 15px;
  justify-content: flex-end;
}

.btn-primary {
  background: linear-gradient(90deg, #60a5fa, #3b82f6);
  color: white;
  border: none;
  padding: 10px 18px;
  border-radius: 8px;
  cursor: pointer;
}

.btn-secondary {
  background: #e5e7eb;
  border: none;
  padding: 10px 18px;
  border-radius: 8px;
  cursor: pointer;
}

.btn-primary:hover,
.btn-secondary:hover {
  opacity: 0.9;
}
</style>
