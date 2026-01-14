<template>
  <div class="dashboard-container">
    
    <section class="welcome">
      <h3>👋 Bienvenue, {{ studentName }}</h3>
      <p>Voici un aperçu de votre progression académique</p>
    </section>

    <section class="stats-cards">
      <div class="card">
        <div class="card-count">{{ stats.completedProjects }}</div>
        <div class="card-title">Projets réalisés</div>
      </div>
      <div class="card">
        <div class="card-count">{{ stats.currentProjects }}</div>
        <div class="card-title">Projet actuel</div>
      </div>
      <div class="card">
        <div class="card-count">{{ stats.unreadMessages }}</div>
        <div class="card-title">Messages non lus</div>
      </div>
      <div class="card">
        <div class="card-count">{{ stats.lateTasks }}</div>
        <div class="card-title">Tâches en retard</div>
      </div>
    </section>

    <section class="current-project">
      <h3>Projet actuel</h3>
      <div class="project-card">
        <h4>{{ currentProject.title }}</h4>
        <p><strong>Encadrant :</strong> {{ currentProject.supervisor }}</p>
        <p>{{ currentProject.description }}</p>
        <div class="progress-bar">
          <div class="progress-fill" :style="{ width: currentProject.progress + '%' }"></div>
        </div>
        <p>Progression : {{ currentProject.progress }}%</p>
        <p>Deadline : {{ currentProject.deadline }}</p>

        <button class="btn" @click="goToProject">Voir le projet</button>
        <button class="btn btn-secondary" @click="openContactModal">Contacter</button>
      </div>
    </section>

    <section class="quick-actions">
      <h3>Actions Rapides</h3>
      <div class="actions-buttons">
        <button class="btn" @click="goToProjects">Mes Projets</button>
        <button class="btn" @click="goToProfile">Mon Profil</button>
        <button class="btn" @click="goToUploadReport">Déposer Rapport</button>
        
      </div>
    </section>

    <div v-if="showContactModal" class="modal-overlay" @click.self="closeContactModal">
      <div class="modal-content">
        <h3>Contacter {{ currentProject.supervisor }}</h3>
        <form @submit.prevent="sendMessage">
          <label for="subject">Objet :</label>
          <input id="subject" v-model="contactForm.subject" type="text" required />

          <label for="message">Message :</label>
          <textarea id="message" v-model="contactForm.message" rows="5" required></textarea>

          <div class="modal-actions">
            <button type="submit" class="btn">Envoyer</button>
            <button type="button" class="btn btn-secondary" @click="closeContactModal">Annuler</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "DashboardView",
  data() {
    return {
      studentName: "Amina Mourad",
      stats: { completedProjects: 4, currentProjects: 1, unreadMessages: 2, lateTasks: 0 },
      currentProject: {
        id: 1,
        title: "Application Mobile de Gestion d'Événements",
        supervisor: "Dr. Karim Benali",
        description: "Développement d'une application mobile pour la gestion d'événements universitaires",
        progress: 65,
        deadline: "2024-06-30"
      },
      showContactModal: false,
      contactForm: { subject: '', message: '' }
    }
  },
  methods: {
    goToProject() {
      this.$router.push({ name: 'ProjectDetails', params: { id: this.currentProject.id } })
    },
    openContactModal() {
      this.showContactModal = true;
    },
    closeContactModal() {
      this.showContactModal = false;
      this.contactForm.subject = '';
      this.contactForm.message = '';
    },
    sendMessage() {
      alert(`Message envoyé à ${this.currentProject.supervisor} !\n\nObjet : ${this.contactForm.subject}\nMessage : ${this.contactForm.message}`);
      this.closeContactModal();
    },
    goToProjects() {
      this.$router.push({ name: 'Projects' }); 
    },
    goToProfile() {
      this.$router.push({ name: 'Profile' });
    },
    goToUploadReport() {
      this.$router.push({ name: 'UploadReport' }); 
    },
    goToMessages() {
      this.$router.push({ name: 'Messages' }); 
    }
  }
}
</script>

<style scoped>

.dashboard-container {
  font-family: Arial, sans-serif;
  padding: 20px;
  background: #f5f5f5;
  min-height: 100vh;
}

.stats-cards {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  margin-bottom: 30px;
}
.card {
  flex: 1 1 200px;
  background: linear-gradient(90deg, #fb923c, #f97316);
  color: white;
  padding: 20px;
  border-radius: 10px;
  text-align: center;
  box-shadow: 0 4px 8px rgba(0,0,0,0.15);
  transition: transform 0.2s;
}
.card:hover {
  transform: translateY(-5px);
}
.card-count {
  font-size: 2.5rem;
  font-weight: bold;
}
.card-title {
  margin-top: 10px;
  font-size: 1rem;
}

.current-project h3 {
  margin-bottom: 10px;
}
.project-card {
  background: white;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0,0,0,0.1);
  margin-bottom: 30px;
}
.progress-bar {
  background: #ddd;
  height: 20px;
  border-radius: 10px;
  overflow: hidden;
  margin: 10px 0;
}
.progress-fill {
  height: 100%;
  background-color: #60a5fa;
  border-radius: 10px 0 0 10px;
}

.btn {
  background: linear-gradient(90deg, #60a5fa, #3b82f6);
  color: white;
  border: none;
  padding: 10px 15px;
  border-radius: 5px;
  cursor: pointer;
  margin-right: 10px;
  margin-top: 10px;
}
.btn-secondary {
  background: linear-gradient(90deg, #f97316, #fb923c);
}

.modal-overlay {
  position: fixed;
  top: 0; left: 0;
  width: 100%; height: 100%;
  background: rgba(0,0,0,0.5);
  display: flex; justify-content: center; align-items: center;
  z-index: 1000;
}
.modal-content {
  background: white;
  padding: 25px;
  border-radius: 10px;
  width: 90%;
  max-width: 500px;
  box-shadow: 0 5px 15px rgba(0,0,0,0.3);
}
.modal-content label {
  display: block;
  margin-top: 10px;
  font-weight: bold;
}
.modal-content input, .modal-content textarea {
  width: 100%;
  padding: 8px 10px;
  margin-top: 5px;
  border-radius: 5px;
  border: 1px solid #ccc;
}
.modal-actions {
  margin-top: 15px;
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}
</style>
