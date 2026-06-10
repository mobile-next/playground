import SwiftUI

struct LoginSuccessfulScreen: View {
    let name: String

    var body: some View {
        VStack(spacing: 16) {
            Image(systemName: "checkmark.circle.fill")
                .font(.system(size: 64))
                .foregroundColor(.green)
                .accessibilityIdentifier("login_success_icon")

            Text("You have successfully logged in to the native app, \(name)!")
                .font(.title2)
                .multilineTextAlignment(.center)
                .padding(.horizontal)
                .accessibilityIdentifier("login_success_message")
        }
        .navigationTitle("Login Successful")
        .navigationBarTitleDisplayMode(.inline)
    }
}

#Preview {
    NavigationStack {
        LoginSuccessfulScreen(name: "Alice")
    }
}
