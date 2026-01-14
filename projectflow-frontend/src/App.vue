<template>
  <div id="app">
   
    <Navbar
      :studentName="studentName"
      @profile-click="goToProfile"
    />

    <router-view />
  </div>
</template>

<script>
import Navbar from './components/Navbar.vue'

export default {
  name: "App",
  components: {
    Navbar
  },
  data() {
    return {
      studentName: "Amina Mourad"
    }
  },
  methods: {
    goToProfile() {
      this.$router.push('/profile')
    }
  }
}
</script>

<style>
body {
  margin: 0;
  font-family: Arial, sans-serif;
  background: #f5f5f5;
}
#app {
  min-height: 100vh;
}
</style>
