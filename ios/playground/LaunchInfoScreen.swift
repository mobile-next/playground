import SwiftUI

// Lists the arguments and environment the app process was launched with, one row each, so a
// test can read them from the UI tree. A VStack, not a List: List only renders visible rows,
// and off-screen rows would be missing from the hierarchy.
struct LaunchInfoScreen: View {
    private let arguments = Array(ProcessInfo.processInfo.arguments.dropFirst())
    private let environment = ProcessInfo.processInfo.environment.sorted { $0.key < $1.key }

    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 12) {
                Text("Arguments").font(.headline)
                if arguments.isEmpty {
                    row("arguments = (none)")
                }
                ForEach(Array(arguments.enumerated()), id: \.offset) { index, argument in
                    row("arg[\(index)] = \(argument)")
                }

                Text("Environment").font(.headline).padding(.top, 12)
                ForEach(environment, id: \.key) { key, value in
                    row("env.\(key) = \(value)")
                }
            }
            .frame(maxWidth: .infinity, alignment: .leading)
            .padding()
        }
        .navigationTitle("Launch Info")
        .navigationBarTitleDisplayMode(.inline)
    }

    // The label doubles as the accessibility id, so a test can find a row by its text.
    private func row(_ text: String) -> some View {
        Text(text)
            .font(.system(.body, design: .monospaced))
            .accessibilityIdentifier(text)
    }
}

#Preview {
    NavigationStack {
        LaunchInfoScreen()
    }
}
