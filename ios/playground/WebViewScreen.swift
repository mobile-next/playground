import SwiftUI
import WebKit

struct WebViewScreen: View {
    @EnvironmentObject var router: AppRouter

    var body: some View {
        DeepLinkWebView(url: URL(string: "https://mobilewright.dev/samples/webview/?source=webview")!) { url in
            guard url.scheme == "playground", url.host == "login-successful" else { return }
            let components = URLComponents(url: url, resolvingAgainstBaseURL: false)
            let name = components?.queryItems?.first(where: { $0.name == "name" })?.value ?? "Guest"
            router.loginSuccessfulName = name
        }
        .ignoresSafeArea(edges: .bottom)
        .navigationTitle("Web View")
        .navigationBarTitleDisplayMode(.inline)
        .navigationBarBackButtonHidden(false)
    }
}

struct DeepLinkWebView: UIViewRepresentable {
    let url: URL
    let onDeepLink: (URL) -> Void

    func makeCoordinator() -> Coordinator {
        Coordinator(onDeepLink: onDeepLink)
    }

    func makeUIView(context: Context) -> WKWebView {
        let webView = WKWebView()
        webView.navigationDelegate = context.coordinator
        webView.load(URLRequest(url: url))
        return webView
    }

    func updateUIView(_ webView: WKWebView, context: Context) {}

    class Coordinator: NSObject, WKNavigationDelegate {
        let onDeepLink: (URL) -> Void

        init(onDeepLink: @escaping (URL) -> Void) {
            self.onDeepLink = onDeepLink
        }

        func webView(_ webView: WKWebView, decidePolicyFor navigationAction: WKNavigationAction, decisionHandler: @escaping (WKNavigationActionPolicy) -> Void) {
            if let url = navigationAction.request.url, url.scheme == "playground" {
                onDeepLink(url)
                decisionHandler(.cancel)
                return
            }
            decisionHandler(.allow)
        }
    }
}

#Preview {
    NavigationStack {
        WebViewScreen()
            .environmentObject(AppRouter())
    }
}
