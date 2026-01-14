<template>
  <div class="stat-card" :class="color">
    <div class="stat-icon">
      <component :is="icon" :size="24" :stroke-width="2" />
    </div>
    <div class="stat-value">{{ value }}</div>
    <div class="stat-label">{{ label }}</div>
    <TrendingUp class="trend-icon" :size="16" />
  </div>
</template>

<script setup>
import { TrendingUp } from 'lucide-vue-next'

defineProps({
  icon: {
    type: Object,
    required: true
  },
  value: {
    type: [String, Number],
    required: true
  },
  label: {
    type: String,
    required: true
  },
  color: {
    type: String,
    default: 'blue',
    validator: (value) => ['blue', 'green', 'yellow', 'purple', 'pink'].includes(value)
  }
})
</script>

<style scoped>
.stat-card {
  position: relative;
}

.trend-icon {
  position: absolute;
  top: 20px;
  right: 20px;
  color: rgba(0, 0, 0, 0.2);
}
</style>