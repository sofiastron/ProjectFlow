<template>
  <div class="progress-wrapper">
    <div class="progress-bar">
      <div 
        class="progress-fill" 
        :class="statusClass"
        :style="{ width: percentage + '%' }"
      ></div>
    </div>
    <div class="progress-label">
      <span class="progress-text">{{ current }}/{{ max }}</span>
      <span class="progress-percent">{{ percentage }}%</span>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  current: {
    type: Number,
    required: true
  },
  max: {
    type: Number,
    required: true
  }
})

const percentage = computed(() => {
  return Math.min((props.current / props.max) * 100, 100)
})

const statusClass = computed(() => {
  const rate = (props.current / props.max) * 100
  if (rate >= 100) return 'sature'
  if (rate >= 60) return 'charge-moyenne'
  return 'disponible'
})
</script>

<style scoped>
.progress-container {
  width: 100%;
}

.progress-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.progress-label {
  display: flex;
  justify-content: space-between;
  font-size: 14px;
  font-weight: 600;
  color: var(--text-dark);
}

.progress-value {
  font-size: 13px;
  font-weight: 600;
  color: var(--text-gray);
}
</style>