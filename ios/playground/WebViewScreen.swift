import SwiftUI
import WebKit

struct WebViewScreen: View {
    var body: some View {
        WebView(url: URL(string: "https://mobilewright.dev")!)
            .ignoresSafeArea(edges: .bottom)
            .navigationTitle("Web View")
            .navigationBarTitleDisplayMode(.inline)
            .navigationBarBackButtonHidden(false)
    }
}

struct WebView: UIViewRepresentable {
    let url: URL

    func makeUIView(context: Context) -> WKWebView {
        let webView = WKWebView()
        webView.load(URLRequest(url: url))
        return webView
    }

    func updateUIView(_ webView: WKWebView, context: Context) {}
}

#Preview {
    NavigationStack {
        WebViewScreen()
    }
}
