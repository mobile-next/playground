import SwiftUI

struct ContentView: View {
    var body: some View {
        NavigationStack {
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
                }
            }
            .navigationBarHidden(true)
        }
    }
}

#Preview {
    ContentView()
}
