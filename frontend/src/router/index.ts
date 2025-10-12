import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import LobbyView from '../views/LobbyView.vue'
import RealLobbyView from '../views/RealLobbyView.vue'
import GameView from '../views/GameView.vue'
import MapCreatorView from '@/views/MapCreatorView.vue'
import MapsView from '@/views/MapsView.vue'
import GameFinishedView from '@/views/GameFinishedView.vue'

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
      meta: { title: 'Maps' },
      component: MapsView,
    },
    {
      path: '/maps/new',
      name: 'map-creator',
      meta: { title: 'Map Creator' },
      component: MapCreatorView,
    },
    {
      path: '/lobbies',
      name: 'lobbies',
      meta: { title: 'Lobbies' },
      component: LobbyView,
    },
    {
      path: '/lobbies/:id',
      name: 'lobby',
      component: RealLobbyView,
      props: true,
    },
    {
      path: '/game/:id',
      name: 'game',
      component: GameView,
      props: route => ({
        id: Number(route.params.id)
      })
    },
    {
      path: '/game/finished',
      name: 'game-finished',
      meta: { title: 'Game Finished' },
      component: GameFinishedView,
      props: route => ({
        id: Number(route.params.id)
      })
    },
  ],
})

export default router
