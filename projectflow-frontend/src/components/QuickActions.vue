<template>
  <div class="quick-actions">
    <router-link to="/projects">Mes Projets</router-link>
    <router-link to="/profile">Mon Profil</router-link>
    <router-link to="/submit">Déposer Rapport</router-link>
    <router-link to="/messages">Messages</router-link>
  </div>
</template>

<script>
export default {}
</script>

<style>
.quick-actions {
  display: flex;
  gap: 20px;
  flex-wrap: wrap;
}

.quick-actions a {
  flex: 1 1 150px;
  text-align: center;
  background: linear-gradient(90deg, #fb923c, #f97316);
  color: white;
  padding: 15px;
  border-radius: 8px;
  text-decoration: none;
  font-weight: bold;
}

.quick-actions a:hover {
  opacity: 0.85;
}
</style>
