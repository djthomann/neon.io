<template></template>

<script lang="ts" setup>
import { onUnmounted, ref } from 'vue'
import { Client, type IMessage } from '@stomp/stompjs'
import SockJS from 'sockjs-client'
import type { LobbyJoin } from '@/assets/types/events/lobbyJoin'
import { useLobbyStore } from '@/stores/lobbies'
import type { Player } from '@/assets/types/player'
import type { LobbyLeave } from '@/assets/types/events/lobbyLeave'
import type { MapChosen } from '@/assets/types/events/mapChosen'
import type { LobbyDelete } from '@/assets/types/events/lobbyDelete'
import router from '@/router'
import type { GameStart } from '@/assets/types/events/gameStart'

const lobbyStore = useLobbyStore()

const props = defineProps<{ lobbyId: Number }>()

const socket = new SockJS('/ws')

const stompClient = new Client({
  webSocketFactory: () => socket,
  reconnectDelay: 5000, // optional
})

stompClient.onConnect = (frame) => {
  console.log('Subscriptions activated!')

  // Subscription for joins
  stompClient.subscribe(`/topic/lobby/${props.lobbyId}/join`, (msg: IMessage) => {
    const data: LobbyJoin = JSON.parse(msg.body)
    const newPlayer: Player = { id: data.playerId, name: data.name }
    lobbyStore.selectedLobby?.players.push(newPlayer)
    console.log('Lobby-Join:', data)
  })

  // Subscription for leaves
  stompClient.subscribe(`/topic/lobby/${props.lobbyId}/leave`, (msg: IMessage) => {
    const data: LobbyLeave = JSON.parse(msg.body)
    if (lobbyStore.selectedLobby) {
      lobbyStore.selectedLobby.players = lobbyStore.selectedLobby.players.filter(
        (p) => p.id !== data.playerId,
      )
    }
    console.log('Lobby-Leave:', data)
  })

  // Subscription for delete
  stompClient.subscribe(`/topic/lobby/${props.lobbyId}/delete`, (msg: IMessage) => {
    const data: LobbyDelete = JSON.parse(msg.body)

    router.push('/lobbies')

    console.log('Lobby-Delete:', data)
  })

  // Subscription for start
  stompClient.subscribe(`/topic/lobby/${props.lobbyId}/start`, (msg: IMessage) => {
    const data: GameStart = JSON.parse(msg.body)

    router.push('/game')

    console.log('Game Start:', data)
  })

  // Subscription for map
  stompClient.subscribe(`/topic/lobby/${props.lobbyId}/map`, (msg: IMessage) => {
    const data: MapChosen = JSON.parse(msg.body)

    if (lobbyStore.selectedLobby) {
      console.log('Lobby selected')
      console.log(lobbyStore.selectedLobby)
      console.log(data)
      lobbyStore.selectedLobby = {
        ...lobbyStore.selectedLobby,
        map: data.map,
      }
      console.log(lobbyStore.selectedLobby.map)
    }

    console.log('Map Chosen:', data)
  })

  // Beispiel: Message senden
  // stompClient.publish({
  //   destination: '/app/lobby/alpha/join',
  //   body: JSON.stringify({ username: 'User1', lobbyId: 'alpha' }),
  // })
}

// Activate client
stompClient.activate()

onUnmounted(() => {
  // Deactive client
  console.log('Connection deactivated')
  stompClient.deactivate()
})
</script>

<style></style>
