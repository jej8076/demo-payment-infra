'use client'

import {useEffect, useState} from 'react'
import {useRouter} from 'next/navigation'

interface Card {
  cardNumber: string
  expiryMonth: string
  expiryYear: string
  cvv: string
  cardRefId: string
}

export default function Payment() {
  const [ci, setCi] = useState('')
  const [cards, setCards] = useState<Card[]>([])
  const [selectedCard, setSelectedCard] = useState<Card | null>(null)
  const [cardForm, setCardForm] = useState({
    cardNumber: '',
    expiryMonth: '',
    expiryYear: '',
    cvv: ''
  })
  const [paymentAmount, setPaymentAmount] = useState('')
  const [sellerId, setSellerId] = useState('')
  const router = useRouter()

  useEffect(() => {
    const storedCi = sessionStorage.getItem('ci')
    if (!storedCi) {
      router.push('/')
      return
    }
    setCi(storedCi)

    const storedCards = sessionStorage.getItem('cards')
    if (storedCards) {
      setCards(JSON.parse(storedCards))
    }
  }, [router])

  const handleLogout = () => {
    sessionStorage.clear()
    router.push('/')
  }

  const handleCardRegister = async (e: React.FormEvent) => {
    e.preventDefault()
    try {
      const response = await fetch('http://localhost:8000/payment/card/register', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({
          ci,
          cardNumber: cardForm.cardNumber,
          expiryDate: cardForm.expiryMonth + "/" + cardForm.expiryYear,
          cvv: cardForm.cvv
        })
      })

      if (response.ok) {
        const result = await response.json()
        const newCard: Card = {...cardForm, cardRefId: result.cardRefId}
        const updatedCards = [...cards, newCard]
        setCards(updatedCards)
        sessionStorage.setItem('cards', JSON.stringify(updatedCards))
        setCardForm({cardNumber: '', expiryMonth: '', expiryYear: '', cvv: ''})
        alert('카드가 등록되었습니다.')
      } else {
        alert('카드 등록에 실패했습니다.')
      }
    } catch (error) {
      alert('카드 등록 중 오류가 발생했습니다.')
    }
  }

  const handlePayment = async (e: React.FormEvent) => {
    e.preventDefault()
    if (!selectedCard) {
      alert('카드를 선택해주세요.')
      return
    }

    try {
      const response = await fetch('http://localhost:8000/payment/pay', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({
          ci,
          cardRefId: selectedCard.cardRefId,
          amount: parseFloat(paymentAmount),
          sellerId
        })
      })

      if (response.ok) {
        alert('결제가 완료되었습니다.')
        setPaymentAmount('')
        setSellerId('')
      } else {
        alert('결제에 실패했습니다.')
      }
    } catch (error) {
      alert('결제 중 오류가 발생했습니다.')
    }
  }

  return (
      <div className="container">
        <div className="header">
          <h1>결제</h1>
          <div style={{display: 'flex', alignItems: 'center', gap: '1rem'}}>
            <span>사용자: {ci}</span>
            <button onClick={handleLogout} className="btn">로그아웃</button>
          </div>
        </div>

        <div className="section">
          <h2 className="section-title">카드 등록</h2>
          <form onSubmit={handleCardRegister}>
            <div className="form-grid">
              <div className="form-group">
                <label>카드번호</label>
                <input
                    type="text"
                    value={cardForm.cardNumber}
                    onChange={(e) => setCardForm({...cardForm, cardNumber: e.target.value})}
                    placeholder="1111-2222-3333-4444"
                    required
                />
              </div>
              <div className="form-group">
                <label>만료일</label>
                <div style={{display: 'flex', gap: '2rem'}}>
                  <input
                      type="text"
                      value={cardForm.expiryMonth}
                      onChange={(e) => setCardForm({...cardForm, expiryMonth: e.target.value})}
                      placeholder="12"
                      maxLength={2}
                      style={{width: '83px'}}
                      required
                  />
                  /
                  <input
                      type="text"
                      value={cardForm.expiryYear}
                      onChange={(e) => setCardForm({...cardForm, expiryYear: e.target.value})}
                      placeholder="25"
                      maxLength={2}
                      style={{width: '83px'}}
                      required
                  />
                </div>
              </div>
              <div className="form-group">
                <label>CVV</label>
                <input
                    type="text"
                    value={cardForm.cvv}
                    onChange={(e) => setCardForm({...cardForm, cvv: e.target.value})}
                    placeholder="123"
                    maxLength={3}
                    required
                />
              </div>
            </div>
            <button type="submit" className="btn btn-primary">카드 등록</button>
          </form>
        </div>

        <div className="section">
          <h2 className="section-title">등록된 카드 ({cards.length}개)</h2>
          <div className="card-list">
            {cards.map((card, index) => (
                <div
                    key={index}
                    className={`card-item ${selectedCard === card ? 'selected' : ''}`}
                    onClick={() => setSelectedCard(card)}
                >
                  <div className="card-info">
                    {card.cardNumber}
                  </div>
                  <div>
                    {card.expiryMonth}/{card.expiryYear}
                  </div>
                </div>
            ))}
            {cards.length === 0 && (
                <div style={{textAlign: 'center', padding: '2rem', color: '#666'}}>
                  등록된 카드가 없습니다
                </div>
            )}
          </div>
        </div>

        <div className="section">
          <h2 className="section-title">결제</h2>
          <form onSubmit={handlePayment}>
            <div className="payment-form">
              <div className="form-group">
                <label>결제금액</label>
                <input
                    type="number"
                    value={paymentAmount}
                    onChange={(e) => setPaymentAmount(e.target.value)}
                    placeholder="10000"
                    required
                />
              </div>
              <div className="form-group">
                <label>판매자 ID</label>
                <input
                    type="text"
                    value={sellerId}
                    onChange={(e) => setSellerId(e.target.value)}
                    placeholder="seller123"
                    required
                />
              </div>
              <button type="submit" className="btn btn-primary">결제하기</button>
            </div>
            {selectedCard && (
                <div style={{
                  marginTop: '1rem',
                  padding: '0.75rem',
                  background: '#f9f9f9',
                  borderRadius: '4px',
                  fontSize: '0.9rem'
                }}>
                  선택된 카드:
                  {selectedCard.cardNumber} ({selectedCard.expiryMonth}/{selectedCard.expiryYear})
                </div>
            )}
          </form>
        </div>
      </div>
  )
}
