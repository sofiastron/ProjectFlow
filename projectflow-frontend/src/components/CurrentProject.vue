<template>
  <div class="project-card">
    <h3 class="title">Projet actuel</h3>

    <p><strong>Nom :</strong> {{ project.name }}</p>
    <p><strong>Encadrant :</strong> {{ project.supervisor }}</p>

    <div class="progress-header">
      <span>Progression du projet</span>
      <span class="percent">{{ project.progress }}%</span>
    </div>

    <div class="progress-bar">
      <div
        class="progress-fill"
        :style="{ width: project.progress + '%' }"
      ></div>
    </div>

    <div class="deadline">
      <p><strong>⏰ Prochain délai</strong></p>
      <p>{{ project.sprint }}</p>
      <p class="days">{{ project.daysLeft }} jours restants</p>
    </div>

    <div class="actions">
      <button class="btn-primary" @click="goToProject">
        Voir le projet
      </button>
      <button class="btn-secondary">
        Contacter
      </button>
    </div>
  </div>
</template>

<script>
export default {
  name: "CurrentProject",
  props: {
    project: {
      type: Object,
      required: true
    }
  },
  methods: {
    goToProject() {
      
      this.$router.push(`/project/${this.project.id}`)
    }
  }
}
</script>

<style scoped>
.project-card {
  background: white;
  padding: 25px;
  border-radius: 16px;
  box-shadow: 0 10px 25px rgba(0,0,0,0.08);
}

.title {
  margin-bottom: 15px;
  color: #f97316;
}

.progress-header {
  display: flex;
  justify-content: space-between;
  margin-top: 15px;
  font-weight: bold;
}

.progress-bar {
  background: #e5e7eb;
  height: 10px;
  border-radius: 6px;
  overflow: hidden;
  margin: 8px 0 15px;
}

.progress-fill {
  height: 100%;
  background: linear-gradient(90deg, #60a5fa, #3b82f6);
}

.deadline {
  background: #fff7ed;
  padding: 12px;
  border-radius: 10px;
  margin-bottom: 15px;
}

.days {
  color: #f97316;
  font-weight: bold;
}

.actions {
  display: flex;
  gap: 10px;
}

.btn-primary {
  background: linear-gradient(90deg, #60a5fa, #3b82f6);
  color: white;
  border: none;
  padding: 10px 16px;
  border-radius: 8px;
  cursor: pointer;
}

.btn-secondary {
  background: #e5e7eb;
  border: none;
  padding: 10px 16px;
  border-radius: 8px;
  cursor: pointer;
}
</style>
