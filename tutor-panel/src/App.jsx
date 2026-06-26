import React, { useState, useEffect } from 'react'
import { BrowserRouter, Routes, Route, Navigate, useNavigate } from 'react-router-dom'
import { initializeApp } from 'firebase/app'
import { getAuth, signInWithEmailAndPassword, signOut, onAuthStateChanged } from 'firebase/auth'
import { getFirestore, doc, getDoc, collection, query, where, onSnapshot } from 'firebase/firestore'
import { Shield, LogOut, Eye, Clock, AlertTriangle, Smartphone, Lock, Activity } from 'lucide-react'

const firebaseConfig = {
  apiKey: import.meta.env.VITE_FIREBASE_API_KEY || '',
  authDomain: import.meta.env.VITE_FIREBASE_AUTH_DOMAIN || '',
  projectId: import.meta.env.VITE_FIREBASE_PROJECT_ID || '',
  storageBucket: import.meta.env.VITE_FIREBASE_STORAGE_BUCKET || '',
  messagingSenderId: import.meta.env.VITE_FIREBASE_MESSAGING_SENDER_ID || '',
  appId: import.meta.env.VITE_FIREBASE_APP_ID || ''
}

const app = initializeApp(firebaseConfig)
const auth = getAuth(app)
const db = getFirestore(app)

function Login({ onLogin }) {
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)

  const handleSubmit = async (e) => {
    e.preventDefault()
    setLoading(true)
    setError('')
    try {
      await signInWithEmailAndPassword(auth, email, password)
      onLogin()
    } catch (err) {
      setError('Credenciales incorrectas. Verifica tu email y contraseña.')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-green-50 to-blue-50">
      <div className="bg-white p-8 rounded-2xl shadow-xl w-full max-w-md">
        <div className="flex items-center justify-center mb-6">
          <Shield className="w-10 h-10 text-green-600 mr-2" />
          <h1 className="text-2xl font-bold text-gray-800">CleanShield Tutor</h1>
        </div>
        <p className="text-center text-gray-500 mb-6">
          Panel de supervisión para tutores y padres
        </p>
        {error && (
          <div className="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-lg mb-4">
            {error}
          </div>
        )}
        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Email</label>
            <input
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent"
              placeholder="tutor@email.com"
              required
            />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Contraseña</label>
            <input
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent"
              placeholder="••••••••"
              required
            />
          </div>
          <button
            type="submit"
            disabled={loading}
            className="w-full bg-green-600 text-white py-2 px-4 rounded-lg hover:bg-green-700 transition-colors disabled:opacity-50"
          >
            {loading ? 'Ingresando...' : 'Iniciar Sesión'}
          </button>
        </form>
      </div>
    </div>
  )
}

function Dashboard({ deviceId, setDeviceId }) {
  const [devices, setDevices] = useState([])
  const [selectedDevice, setSelectedDevice] = useState(null)
  const [activityLog, setActivityLog] = useState([])
  const [stats, setStats] = useState({ blockedToday: 0, streakDays: 0, panicUses: 0 })
  const navigate = useNavigate()

  useEffect(() => {
    const user = auth.currentUser
    if (!user) return

    const devicesRef = collection(db, 'tutors', user.uid, 'devices')
    const unsubscribe = onSnapshot(devicesRef, (snapshot) => {
      const deviceList = snapshot.docs.map(doc => ({ id: doc.id, ...doc.data() }))
      setDevices(deviceList)
      if (deviceList.length > 0 && !selectedDevice) {
        setSelectedDevice(deviceList[0])
        setDeviceId(deviceList[0].id)
      }
    })

    return () => unsubscribe()
  }, [])

  useEffect(() => {
    if (!selectedDevice) return

    const activityRef = collection(db, 'devices', selectedDevice.id, 'activity')
    const q = query(activityRef, where('timestamp', '>=', new Date(Date.now() - 86400000)))
    const unsubscribe = onSnapshot(q, (snapshot) => {
      const logs = snapshot.docs.map(doc => ({ id: doc.id, ...doc.data() }))
      setActivityLog(logs.sort((a, b) => b.timestamp - a.timestamp))
      setStats({
        blockedToday: logs.filter(l => l.type === 'blocked').length,
        streakDays: selectedDevice.streakDays || 0,
        panicUses: logs.filter(l => l.type === 'panic').length
      })
    })

    return () => unsubscribe()
  }, [selectedDevice])

  const handleLogout = async () => {
    await signOut(auth)
    navigate('/')
  }

  return (
    <div className="min-h-screen bg-gray-50">
      <header className="bg-white shadow-sm border-b">
        <div className="max-w-7xl mx-auto px-4 py-4 flex items-center justify-between">
          <div className="flex items-center">
            <Shield className="w-8 h-8 text-green-600 mr-2" />
            <h1 className="text-xl font-bold text-gray-800">Panel de Tutor</h1>
          </div>
          <div className="flex items-center gap-4">
            <select
              value={selectedDevice?.id || ''}
              onChange={(e) => {
                const device = devices.find(d => d.id === e.target.value)
                setSelectedDevice(device)
                setDeviceId(device?.id || '')
              }}
              className="px-3 py-2 border border-gray-300 rounded-lg text-sm"
            >
              {devices.map(device => (
                <option key={device.id} value={device.id}>
                  {device.name || device.id}
                </option>
              ))}
            </select>
            <button
              onClick={handleLogout}
              className="flex items-center text-gray-500 hover:text-red-500 transition-colors"
            >
              <LogOut className="w-5 h-5" />
            </button>
          </div>
        </div>
      </header>

      <main className="max-w-7xl mx-auto px-4 py-8">
        <div className="grid grid-cols-1 md:grid-cols-4 gap-6 mb-8">
          <StatCard
            icon={<Lock className="w-6 h-6 text-red-500" />}
            label="Bloqueos Hoy"
            value={stats.blockedToday}
            color="red"
          />
          <StatCard
            icon={<Activity className="w-6 h-6 text-green-500" />}
            label="Racha (días)"
            value={stats.streakDays}
            color="green"
          />
          <StatCard
            icon={<AlertTriangle className="w-6 h-6 text-yellow-500" />}
            label="Botón Pánico"
            value={stats.panicUses}
            color="yellow"
          />
          <StatCard
            icon={<Smartphone className="w-6 h-6 text-blue-500" />}
            label="Dispositivo"
            value={selectedDevice?.status === 'online' ? 'Online' : 'Offline'}
            color="blue"
          />
        </div>

        <div className="bg-white rounded-xl shadow-sm border p-6">
          <h2 className="text-lg font-semibold text-gray-800 mb-4 flex items-center">
            <Clock className="w-5 h-5 mr-2 text-gray-500" />
            Actividad Reciente (últimas 24h)
          </h2>
          {activityLog.length === 0 ? (
            <p className="text-gray-400 text-center py-8">
              No hay actividad registrada en las últimas 24 horas.
            </p>
          ) : (
            <div className="space-y-3">
              {activityLog.map((log) => (
                <div key={log.id} className="flex items-center justify-between p-3 bg-gray-50 rounded-lg">
                  <div className="flex items-center">
                    {log.type === 'blocked' && <Lock className="w-4 h-4 text-red-500 mr-3" />}
                    {log.type === 'panic' && <AlertTriangle className="w-4 h-4 text-yellow-500 mr-3" />}
                    {log.type === 'access' && <Eye className="w-4 h-4 text-blue-500 mr-3" />}
                    <div>
                      <p className="text-sm font-medium text-gray-700">{log.description || log.type}</p>
                      <p className="text-xs text-gray-400">{log.category || 'General'}</p>
                    </div>
                  </div>
                  <span className="text-xs text-gray-400">
                    {log.timestamp?.toDate ? log.timestamp.toDate().toLocaleTimeString('es-ES') : ''}
                  </span>
                </div>
              ))}
            </div>
          )}
        </div>
      </main>
    </div>
  )
}

function StatCard({ icon, label, value, color }) {
  return (
    <div className="bg-white rounded-xl shadow-sm border p-6">
      <div className="flex items-center justify-between mb-2">
        {icon}
      </div>
      <p className="text-2xl font-bold text-gray-800">{value}</p>
      <p className="text-sm text-gray-500">{label}</p>
    </div>
  )
}

function App() {
  const [user, setUser] = useState(null)
  const [loading, setLoading] = useState(true)
  const [deviceId, setDeviceId] = useState('')

  useEffect(() => {
    const unsubscribe = onAuthStateChanged(auth, (currentUser) => {
      setUser(currentUser)
      setLoading(false)
    })
    return () => unsubscribe()
  }, [])

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-gray-50">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-green-600"></div>
      </div>
    )
  }

  return (
    <BrowserRouter>
      <Routes>
        <Route
          path="/"
          element={
            user ? <Navigate to="/dashboard" /> : <Login onLogin={() => {}} />
          }
        />
        <Route
          path="/dashboard"
          element={
            user ? <Dashboard deviceId={deviceId} setDeviceId={setDeviceId} /> : <Navigate to="/" />
          }
        />
      </Routes>
    </BrowserRouter>
  )
}

export default App
