<template>
  <nav class="navbar">
    <div class="navbar-left">
      
      <span class="navbar-subtitle">Espace Étudiant</span>
    </div>

    <div class="navbar-right">
      <button class="profile-btn" @click="$emit('profile-click')">
        {{ studentName }}
      </button>
    </div>
  </nav>
</template>

<script>
export default {
  name: "AppHeader",
  props: {
    studentName: {
      type: String,
      default: "Étudiant"
    }
  }
}
</script>

<style scoped>
.navbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 30px;
  background: linear-gradient(90deg, #fb923c, #f97316); 
  color: white;
  box-shadow: 0 2px 6px rgba(0,0,0,0.15);
}

.navbar-title {
  margin: 0;
  font-size: 1.7rem;
  font-weight: bold;
}

.navbar-subtitle {
  font-size: 0.9rem;
  opacity: 0.9;
}


.profile-btn {
  background: rgba(255,255,255,0.2);
  border: none;
  padding: 8px 16px;
  border-radius: 8px;
  color: white;
  font-weight: bold;
  cursor: pointer;
  transition: background 0.2s;
}

.profile-btn:hover {
  background: rgba(255,255,255,0.35);
}
</style>
