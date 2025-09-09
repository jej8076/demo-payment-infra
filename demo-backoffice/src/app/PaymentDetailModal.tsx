interface PaymentDetail {
  id: number;
  ci: string;
  cardRefId: string;
  amount: number;
  sellerId: string;
  status: string;
  createdAt: string;
  payList: {
    id: number;
    status: string;
    tokenValue: string;
    createdAt: string;
  }[];
}

interface PaymentDetailModalProps {
  isOpen: boolean;
  onClose: () => void;
  paymentId: number;
}

export default function PaymentDetailModal({ isOpen, onClose, paymentId }: PaymentDetailModalProps) {
  if (!isOpen) return null;

  // Mock data based on the provided response structure
  const mockPaymentDetail: PaymentDetail = {
    id: paymentId,
    ci: "USER-005",
    cardRefId: "card_ref_dae2b97b-8156-4508-8445-fa96e085efc9",
    amount: 100.00,
    sellerId: "SELLER-01",
    status: "APPROVED",
    createdAt: "2025-09-09 17:46:39",
    payList: [
      {
        id: 166,
        status: "APPROVED",
        tokenValue: "token-card_ref_dae2b97b-8156-4508-8445-fa96e085efc9f09a9ba2-9489-4ce7-af22-ec55b1680a36",
        createdAt: "2025-09-09 21:21:35"
      },
      {
        id: 167,
        status: "APPROVED",
        tokenValue: "token-card_ref_dae2b97b-8156-4508-8445-fa96e085efc97bf2e716-8a3d-469a-b044-73605335fc7d",
        createdAt: "2025-09-09 21:22:10"
      }
    ]
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div className="bg-white rounded-lg p-6 max-w-4xl w-full mx-4 max-h-[90vh] overflow-y-auto">
        <div className="flex justify-between items-center mb-4">
          <h2 className="text-xl font-bold">결제 상세 내역</h2>
          <button onClick={onClose} className="text-gray-500 hover:text-gray-700">
            ✕
          </button>
        </div>

        <div className="mb-6">
          <h3 className="text-lg font-semibold mb-3">기본 정보</h3>
          <div className="grid grid-cols-2 gap-4">
            <div><strong>결제 ID:</strong> {mockPaymentDetail.id}</div>
            <div><strong>CI:</strong> {mockPaymentDetail.ci}</div>
            <div><strong>카드 참조 ID:</strong> {mockPaymentDetail.cardRefId}</div>
            <div><strong>금액:</strong> {mockPaymentDetail.amount.toLocaleString()}원</div>
            <div><strong>판매자 ID:</strong> {mockPaymentDetail.sellerId}</div>
            <div><strong>상태:</strong> 
              <span className={`ml-2 px-2 py-1 rounded text-sm ${
                mockPaymentDetail.status === 'APPROVED' ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'
              }`}>
                {mockPaymentDetail.status}
              </span>
            </div>
            <div><strong>생성일시:</strong> {mockPaymentDetail.createdAt}</div>
          </div>
        </div>

        <div>
          <h3 className="text-lg font-semibold mb-3">결제 내역</h3>
          <div className="border rounded">
            <table className="w-full">
              <thead className="bg-gray-50">
                <tr>
                  <th className="p-3 text-left">ID</th>
                  <th className="p-3 text-left">상태</th>
                  <th className="p-3 text-left">토큰 값</th>
                  <th className="p-3 text-left">생성일시</th>
                </tr>
              </thead>
              <tbody>
                {mockPaymentDetail.payList.map((pay) => (
                  <tr key={pay.id} className="border-t">
                    <td className="p-3">{pay.id}</td>
                    <td className="p-3">
                      <span className={`px-2 py-1 rounded text-sm ${
                        pay.status === 'APPROVED' ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'
                      }`}>
                        {pay.status}
                      </span>
                    </td>
                    <td className="p-3 font-mono text-sm">{pay.tokenValue}</td>
                    <td className="p-3">{pay.createdAt}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  );
}
