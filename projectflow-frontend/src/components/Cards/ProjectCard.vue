<template>
  <div class="card">
    <div class="card-count">{{ count }}</div>
    <div class="card-title">{{ title }}</div>
  </div>
</template>

<script>
export default {
  props: {
    title: String,
    count: Number
  }
}
</script>

<style>
.card {
  flex: 1 1 200px;
  background: linear-gradient(90deg, #fb923c, #f97316);
  color: white;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0,0,0,0.1);
  text-align: center;
}
.card-count {
  font-size: 2em;
  font-weight: bold;
}
.card-title {
  margin-top: 10px;
}
</style>
