import type { NeonMap } from '@/assets/types/map'

export async function getMaps(): Promise<NeonMap[]> {
  const res = await fetch('/api/maps', { method: 'GET' })
  if (!res.ok) {
    // error.value = "Error when getting maps"
  }
  const data = await res.json()
  return data as NeonMap[]
}
