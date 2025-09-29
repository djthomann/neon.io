import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import LobbyView from '../views/LobbyView.vue'
import RealLobbyView from '../views/RealLobbyView.vue'
import GameView from '../views/GameView.vue'
import MapCreatorView from '@/views/MapCreatorView.vue'
import MapsView from '@/views/MapsView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
    },
    {
      path: '/maps',
      name: 'maps',
      component: MapsView,
    },
    {
      path: '/maps/new',
      name: 'map-creator',
      component: MapCreatorView,
    },
    {
      path: '/lobbies',
      name: 'lobbies',
      component: LobbyView,
    },
    {
      path: '/lobbies/:id',
      name: 'lobby',
      component: RealLobbyView,
      props: true,
    },
    {
      path: '/game',
      name: 'game',
      component: GameView,
    },
  ],
})

export default router
