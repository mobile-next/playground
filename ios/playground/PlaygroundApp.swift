import SwiftUI

@main
struct PlaygroundApp: App {
    @StateObject private var router = AppRouter()

    var body: some Scene {
        WindowGroup {
            ContentView()
                .environmentObject(router)
                .onOpenURL { url in
                    guard url.scheme == "playground" else { return }
                    if url.host == "login-successful" {
                        let components = URLComponents(url: url, resolvingAgainstBaseURL: false)
                        let name = components?.queryItems?.first(where: { $0.name == "name" })?.value ?? "Guest"
                        router.loginSuccessfulName = name
                    }
                }
        }
    }
}
