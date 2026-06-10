import SwiftUI

enum AppRoute: Hashable {
    case loginSuccessful(name: String)
}

struct ContentView: View {
    @EnvironmentObject var router: AppRouter
    @State private var path = NavigationPath()

    var body: some View {
        NavigationStack(path: $path) {
            VStack(spacing: 0) {
                AsyncImage(url: URL(string: "https://mobilewright.dev/mobilewright-logo.png")) { image in
                    image
                        .resizable()
                        .scaledToFit()
                } placeholder: {
                    ProgressView()
                }
                .frame(height: 100)
                .padding(.vertical, 32)

                List {
                    NavigationLink("Basic UI", destination: BasicUIScreen())
                    NavigationLink("Web View", destination: WebViewScreen())
                    NavigationLink("SharedPref / Keychain", destination: PreferencesScreen())
                    NavigationLink("Continuous Animation", destination: ContinuousAnimationScreen())
                    NavigationLink("Permissions and Alerts", destination: PermissionsScreen())
                }
            }
            .navigationBarHidden(true)
            .navigationDestination(for: AppRoute.self) { route in
                switch route {
                case .loginSuccessful(let name):
                    LoginSuccessfulScreen(name: name)
                }
            }
        }
        .onChange(of: router.loginSuccessfulName) { _, name in
            if let name {
                path.append(AppRoute.loginSuccessful(name: name))
                router.loginSuccessfulName = nil
            }
        }
    }
}

#Preview {
    ContentView()
        .environmentObject(AppRouter())
}
