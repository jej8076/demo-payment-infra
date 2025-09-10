'use client'

import { useState, useEffect } from 'react'
import { useRouter } from 'next/navigation'

export default function Login() {
  const [id, setId] = useState('')
  const router = useRouter()

  useEffect(() => {
    if (sessionStorage.getItem('ci')) {
      router.push('/payment')
    }
  }, [router])

  const handleLogin = (e: React.FormEvent) => {
    e.preventDefault()
    if (id.trim()) {
      sessionStorage.setItem('ci', id.trim())
      router.push('/payment')
    }
  }

  return (
    <div className="login-container">
      <h1 className="login-title">로그인</h1>
      <form onSubmit={handleLogin}>
        <div className="form-group">
          <label>사용자 ID</label>
          <input
            type="text"
            value={id}
            onChange={(e) => setId(e.target.value)}
            placeholder="ID를 입력하세요"
            required
          />
        </div>
        <button type="submit" className="btn btn-primary" style={{width: '100%', marginTop: '1rem'}}>
          로그인
        </button>
      </form>
    </div>
  )
}
