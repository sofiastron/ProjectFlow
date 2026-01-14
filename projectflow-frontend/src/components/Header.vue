<template>
  <header class="header">
    <div class="header-left">
      <div class="search-container">
        <Search :size="18" class="search-icon" />
        <input 
          type="text" 
          placeholder="Rechercher un étudiant, encadrant..." 
          v-model="searchQuery"
          @focus="searchFocused = true"
          @blur="searchFocused = false"
        />
        <kbd v-if="!searchQuery" class="keyboard-shortcut">⌘K</kbd>
      </div>
    </div>

    <div class="header-right">
      <!-- Bouton d'aide -->
      

      <!-- Notifications avec dropdown -->
      <div class="notification-wrapper">
        <button 
          class="icon-btn notification-btn" 
          @click="toggleNotifications"
          :class="{ active: showNotifications }"
        >
          <Bell :size="20" />
          <span v-if="unreadCount > 0" class="badge">{{ unreadCount }}</span>
        </button>

        <!-- Dropdown notifications -->
        
      </div>

      <!-- Profile -->
      
    </div>
  </header>
</template>

<script setup>
import { ref } from 'vue'
import { 
  Search, 
  Bell, 
  HelpCircle, 
  ChevronDown,
  FileText,
  UserCheck,
  Calendar,
  AlertCircle
} from 'lucide-vue-next'

const searchQuery = ref('')
const searchFocused = ref(false)
const showNotifications = ref(false)
const showProfile = ref(false)
const unreadCount = ref(3)

const notifications = ref([
  {
    id: 1,
    type: 'report',
    message: 'Nouveau rapport soumis par Ahmed Bennani',
    time: 'Il y a 2h',
    read: false
  },
  {
    id: 2,
    type: 'user',
    message: 'Affectation automatique complétée - 12 étudiants',
    time: 'Il y a 5h',
    read: false
  },
  {
    id: 3,
    type: 'calendar',
    message: 'Soutenance planifiée pour demain à 10h',
    time: 'Il y a 1j',
    read: false
  },
  {
    id: 4,
    type: 'alert',
    message: '5 étudiants non affectés',
    time: 'Il y a 2j',
    read: true
  }
])

const getNotifIcon = (type) => {
  const icons = {
    report: FileText,
    user: UserCheck,
    calendar: Calendar,
    alert: AlertCircle
  }
  return icons[type] || Bell
}

const toggleNotifications = () => {
  showNotifications.value = !showNotifications.value
  showProfile.value = false
}

const toggleProfile = () => {
  showProfile.value = !showProfile.value
  showNotifications.value = false
}

const markAllAsRead = () => {
  notifications.value.forEach(n => n.read = true)
  unreadCount.value = 0
}
</script>

<style scoped>
.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 32px;
  padding: 16px 0;
}

/* Left side */
.header-left {
  flex: 1;
  max-width: 500px;
}

.search-container {
  position: relative;
  display: flex;
  align-items: center;
  background: var(--bg-white);
  border: 2px solid #E5E5E5;
  border-radius: var(--radius-lg);
  padding: 12px 16px;
  transition: all 0.3s ease;
}

.search-container:focus-within {
  border-color: var(--primary-color);
  box-shadow: 0 0 0 4px rgba(101, 67, 58, 0.1);
}

.search-icon {
  color: var(--text-gray);
  flex-shrink: 0;
}

.search-container input {
  flex: 1;
  border: none;
  outline: none;
  margin-left: 12px;
  font-size: 14px;
  color: var(--text-dark);
  background: transparent;
}

.search-container input::placeholder {
  color: var(--text-light);
}

.keyboard-shortcut {
  padding: 4px 8px;
  background: #F5F5F5;
  border: 1px solid #E0E0E0;
  border-radius: 4px;
  font-size: 12px;
  font-weight: 600;
  color: var(--text-gray);
  font-family: inherit;
}

/* Right side */
.header-right {
  display: flex;
  align-items: center;
  gap: 12px;
}

.icon-btn {
  position: relative;
  width: 44px;
  height: 44px;
  border-radius: var(--radius-md);
  background: var(--bg-white);
  border: 2px solid #E5E5E5;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.2s ease;
  color: var(--text-dark);
}

.icon-btn:hover {
  background: var(--bg-beige);
  border-color: var(--primary-color);
  transform: translateY(-2px);
}

.icon-btn.active {
  background: var(--primary-color);
  border-color: var(--primary-color);
  color: white;
}

.badge {
  position: absolute;
  top: -6px;
  right: -6px;
  min-width: 20px;
  height: 20px;
  padding: 0 6px;
  background: #FF4757;
  color: white;
  border-radius: var(--radius-full);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 11px;
  font-weight: 700;
  border: 2px solid var(--bg-main);
}

/* Notifications dropdown */
.notification-wrapper {
  position: relative;
}

.notifications-dropdown {
  position: absolute;
  top: calc(100% + 12px);
  right: 0;
  width: 380px;
  background: white;
  border-radius: var(--radius-lg);
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.15);
  border: 1px solid #E5E5E5;
  z-index: 1000;
  overflow: hidden;
}

.dropdown-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 20px;
  border-bottom: 1px solid #F0F0F0;
}

.dropdown-header h3 {
  margin: 0;
  font-size: 16px;
  font-weight: 700;
  color: var(--text-dark);
}

.mark-all-read {
  background: none;
  border: none;
  color: var(--primary-color);
  font-size: 13px;
  font-weight: 600;
  cursor: pointer;
  padding: 4px 8px;
  border-radius: var(--radius-sm);
  transition: all 0.2s;
}

.mark-all-read:hover {
  background: rgba(101, 67, 58, 0.1);
}

.notifications-list {
  max-height: 400px;
  overflow-y: auto;
}

.notification-item {
  display: flex;
  gap: 12px;
  padding: 16px 20px;
  border-bottom: 1px solid #F8F8F8;
  cursor: pointer;
  transition: all 0.2s;
  position: relative;
}

.notification-item:hover {
  background: #FAFAFA;
}

.notification-item.unread {
  background: #F0F9FF;
}

.notification-item.unread:hover {
  background: #E0F2FE;
}

.notif-icon {
  width: 40px;
  height: 40px;
  border-radius: var(--radius-md);
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.notif-icon.report {
  background: #DBEAFE;
  color: #3B82F6;
}

.notif-icon.user {
  background: #D1FAE5;
  color: #10B981;
}

.notif-icon.calendar {
  background: #E9D5FF;
  color: #8B5CF6;
}

.notif-icon.alert {
  background: #FEE2E2;
  color: #EF4444;
}

.notif-content {
  flex: 1;
}

.notif-message {
  font-size: 14px;
  color: var(--text-dark);
  margin: 0 0 4px 0;
  line-height: 1.4;
}

.notif-time {
  font-size: 12px;
  color: var(--text-gray);
}

.unread-dot {
  width: 8px;
  height: 8px;
  background: #3B82F6;
  border-radius: 50%;
  flex-shrink: 0;
  margin-top: 6px;
}

.empty-notifs {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 48px 20px;
  color: var(--text-gray);
}

.empty-notifs svg {
  opacity: 0.3;
  margin-bottom: 12px;
}

.empty-notifs p {
  margin: 0;
  font-size: 14px;
}

.dropdown-footer {
  padding: 12px 20px;
  border-top: 1px solid #F0F0F0;
}

.view-all-btn {
  width: 100%;
  padding: 10px;
  background: none;
  border: none;
  color: var(--primary-color);
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  border-radius: var(--radius-md);
  transition: all 0.2s;
}

.view-all-btn:hover {
  background: rgba(101, 67, 58, 0.1);
}

/* Profile */
.profile-btn {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 6px 12px 6px 6px;
  background: var(--bg-white);
  border: 2px solid #E5E5E5;
  border-radius: var(--radius-lg);
  cursor: pointer;
  transition: all 0.2s ease;
}

.profile-btn:hover {
  border-color: var(--primary-color);
  background: var(--bg-beige);
}

.avatar {
  width: 40px;
  height: 40px;
  border-radius: var(--radius-md);
  background: linear-gradient(135deg, var(--primary-color), #9A7266);
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-weight: 700;
  font-size: 14px;
}

.profile-info {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  gap: 2px;
}

.profile-name {
  font-size: 14px;
  font-weight: 600;
  color: var(--text-dark);
}

.profile-role {
  font-size: 12px;
  color: var(--text-gray);
}

.chevron {
  color: var(--text-gray);
}

/* Animations */
.dropdown-enter-active,
.dropdown-leave-active {
  transition: all 0.3s ease;
}

.dropdown-enter-from {
  opacity: 0;
  transform: translateY(-10px);
}

.dropdown-leave-to {
  opacity: 0;
  transform: translateY(-10px);
}

/* Responsive */
@media (max-width: 768px) {
  .header {
    flex-direction: column;
    gap: 16px;
  }

  .header-left {
    max-width: 100%;
    width: 100%;
  }

  .header-right {
    width: 100%;
    justify-content: space-between;
  }

  .notifications-dropdown {
    width: calc(100vw - 32px);
    right: -100px;
  }

  .profile-info {
    display: none;
  }
}
</style>