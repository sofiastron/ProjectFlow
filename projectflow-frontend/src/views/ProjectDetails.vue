<template>
  <div class="project-details">
    <h2>📘 Détails du projet</h2>
    <div class="card">
      <p><strong>Nom du projet :</strong> {{ project.name }}</p>
      <p><strong>Encadrant :</strong> {{ project.supervisor }}</p>
      <p><strong>Description :</strong> {{ project.description }}</p>
    </div>

    <div class="card">
      <p><strong>Progression :</strong></p>
      <div class="progress-bar">
        <div class="progress" :style="{ width: project.progress + '%' }">{{ project.progress }}%</div>
      </div>
    </div>

    <div class="card">
      <p><strong>Sprint actuel :</strong></p>
      <ul>
        <li><strong>Nom :</strong> {{ project.sprint.name }}</li>
        <li><strong>Date limite :</strong> {{ project.sprint.deadline }}</li>
        <li><strong>Jours restants :</strong> {{ project.sprint.daysLeft }}</li>
      </ul>
    </div>

    <button class="btn" @click="$router.push('/')">⬅ Retour au Dashboard</button>
  </div>
</template>

<script>
export default {
  name: "ProjectDetails",
  props: ['id'],   
  data() {
    return {
      project: {
        id: 1,
        name: "Application Mobile de Gestion d'Événements",
        supervisor: "Dr. Karim Benali",
        description: "Développement d'une application mobile pour la gestion d'événements universitaires",
        progress: 65,
        sprint: { name: "Sprint 3", deadline: "15 Déc 2025", daysLeft: 13 }
      }
    }
  }
}
</script>

<style scoped>
.project-details {
  padding: 30px;
  max-width: 800px;
  margin: auto;
}

h2 {
  margin-bottom: 20px;
}

.card {
  background: white;
  padding: 20px;
  margin-bottom: 15px;
  border-radius: 10px;
  box-shadow: 0 2px 6px rgba(0,0,0,0.1);
}

.progress-bar {
  background: #eee;
  border-radius: 20px;
  overflow: hidden;
}

.progress {
  background: linear-gradient(to right, #fb923c, #f97316);
  color: white;
  text-align: center;
  padding: 5px 0;
}

.btn {
  background: linear-gradient(to right, #60a5fa, #3b82f6);
  color: white;
  border: none;
  padding: 10px 18px;
  border-radius: 8px;
  cursor: pointer;
}
</style>
