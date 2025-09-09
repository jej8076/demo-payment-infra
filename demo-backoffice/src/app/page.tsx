'use client';

import {useEffect, useState} from 'react';
import PaymentDetailModal from './PaymentDetailModal';

interface Payment {
  id: number;
  ci: string;
  cardRefId: string;
  amount: number;
  sellerId: string;
  status: string;
  createdAt: string;
}

export default function Home() {
  const [payments, setPayments] = useState<Payment[]>([]);
  const [ci, setCi] = useState('');
  const [cardRefId, setCardRefId] = useState('');
  const [loading, setLoading] = useState(false);
  const [selectedPaymentId, setSelectedPaymentId] = useState<number | null>(null);
  const [isModalOpen, setIsModalOpen] = useState(false);

  const fetchPayments = async () => {
    setLoading(true);
    try {
      const params = new URLSearchParams();
      if (ci) params.append('ci', ci);
      if (cardRefId) params.append('cardRefId', cardRefId);

      const response = await fetch(`http://localhost:8000/view/payments?${params}`);
      const data = await response.json();
      setPayments(data.content || []);
    } catch (error) {
      console.error('Error:', error);
    }
    setLoading(false);
  };

  const handleRowClick = (paymentId: number) => {
    setSelectedPaymentId(paymentId);
    setIsModalOpen(true);
  };

  const handleCloseModal = () => {
    setIsModalOpen(false);
    setSelectedPaymentId(null);
  };

  useEffect(() => {
    fetchPayments();
  }, []);

  return (
      <div className="p-8 max-w-6xl mx-auto">
        <h1 className="text-2xl font-bold mb-6">거래내역 조회</h1>

        <div className="mb-6 flex gap-4">
          <input
              type="text"
              placeholder="CI"
              value={ci}
              onChange={(e) => setCi(e.target.value)}
              className="border px-3 py-2 rounded"
          />
          <input
              type="text"
              placeholder="Card Reference ID"
              value={cardRefId}
              onChange={(e) => setCardRefId(e.target.value)}
              className="border px-3 py-2 rounded"
          />
          <button
              onClick={fetchPayments}
              disabled={loading}
              className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600 disabled:opacity-50"
          >
            {loading ? '검색중...' : '검색'}
          </button>
        </div>

        <div className="border rounded">
          <table className="w-full">
            <thead className="bg-gray-50">
            <tr>
              <th className="p-3 text-left">ID</th>
              <th className="p-3 text-left">CI</th>
              <th className="p-3 text-left">카드참조ID</th>
              <th className="p-3 text-left">금액</th>
              <th className="p-3 text-left">판매자ID</th>
              <th className="p-3 text-left">상태</th>
              <th className="p-3 text-left">생성일시</th>
            </tr>
            </thead>
            <tbody>
            {payments.map((payment) => (
                <tr 
                  key={payment.id} 
                  className="border-t hover:bg-gray-50 cursor-pointer"
                  onClick={() => handleRowClick(payment.id)}
                >
                  <td className="p-3">{payment.id}</td>
                  <td className="p-3">{payment.ci}</td>
                  <td className="p-3">{payment.cardRefId}</td>
                  <td className="p-3">{payment.amount.toLocaleString()}원</td>
                  <td className="p-3">{payment.sellerId}</td>
                  <td className="p-3">
                  <span className={`px-2 py-1 rounded text-sm ${
                      payment.status === 'SUCCESS' ? 'bg-green-100 text-green-800' :
                          payment.status === 'FAIL' ? 'bg-red-100 text-red-800' :
                              'bg-yellow-100 text-yellow-800'
                  }`}>
                    {payment.status}
                  </span>
                  </td>
                  <td className="p-3">{new Date(payment.createdAt).toLocaleString()}</td>
                </tr>
            ))}
            </tbody>
          </table>
          {payments.length === 0 && !loading && (
              <div className="p-8 text-center text-gray-500">조회된 거래내역이 없습니다.</div>
          )}
        </div>

        <PaymentDetailModal 
          isOpen={isModalOpen}
          onClose={handleCloseModal}
          paymentId={selectedPaymentId || 0}
        />
      </div>
  );
}
