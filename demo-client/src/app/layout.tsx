import './globals.css'

export const metadata = {
  title: 'Payment Demo',
  description: 'Payment system demo application',
}

export default function RootLayout({
  children,
}: {
  children: React.ReactNode
}) {
  return (
    <html lang="ko">
      <body>{children}</body>
    </html>
  )
}
