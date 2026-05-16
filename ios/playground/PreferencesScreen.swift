import SwiftUI
import Security

struct PreferencesScreen: View {
    @State private var username = ""
    @State private var password = ""
    @State private var isPasswordRevealed = false
    @State private var statusMessage = ""

    private let service = "com.mobilenext.playground"
    private let usernameKey = "username"
    private let passwordKey = "password"

    var body: some View {
        Form {
            Section {
                TextField("Username", text: $username)
                    .accessibilityIdentifier("username_field")
                    .autocorrectionDisabled()

                HStack {
                    Group {
                        if isPasswordRevealed {
                            TextField("Password", text: $password)
                        } else {
                            SecureField("Password", text: $password)
                        }
                    }
                    .accessibilityIdentifier("password_field")
                    .autocorrectionDisabled()

                    Button(action: { isPasswordRevealed.toggle() }) {
                        Image(systemName: isPasswordRevealed ? "eye.slash" : "eye")
                            .foregroundColor(.secondary)
                    }
                    .accessibilityIdentifier("reveal_button")
                }
            }

            Section {
                HStack(spacing: 0) {
                    Button("Save") { save() }
                        .accessibilityIdentifier("save_button")
                        .buttonStyle(.borderless)
                        .frame(maxWidth: .infinity)
                    Divider()
                    Button("Load") { load() }
                        .accessibilityIdentifier("load_button")
                        .buttonStyle(.borderless)
                        .frame(maxWidth: .infinity)
                    Divider()
                    Button("Delete", role: .destructive) { delete() }
                        .accessibilityIdentifier("delete_button")
                        .buttonStyle(.borderless)
                        .frame(maxWidth: .infinity)
                }
            }

            if !statusMessage.isEmpty {
                Section("Status") {
                    Text(statusMessage)
                        .accessibilityIdentifier("status_message")
                        .foregroundColor(.secondary)
                }
            }
        }
        .navigationTitle("Preferences")
        .navigationBarTitleDisplayMode(.inline)
    }

    private func save() {
        saveToKeychain(key: usernameKey, value: username)
        saveToKeychain(key: passwordKey, value: password)
        statusMessage = "Saved"
    }

    private func load() {
        let storedUsername = loadFromKeychain(key: usernameKey)
        let storedPassword = loadFromKeychain(key: passwordKey)
        guard storedUsername != nil || storedPassword != nil else {
            statusMessage = "No preferences found"
            return
        }
        username = storedUsername ?? ""
        password = storedPassword ?? ""
        statusMessage = "Loaded"
    }

    private func delete() {
        deleteFromKeychain(key: usernameKey)
        deleteFromKeychain(key: passwordKey)
        username = ""
        password = ""
        statusMessage = "Deleted"
    }

    private func saveToKeychain(key: String, value: String) {
        deleteFromKeychain(key: key)
        let query: [String: Any] = [
            kSecClass as String: kSecClassGenericPassword,
            kSecAttrService as String: service,
            kSecAttrAccount as String: key,
            kSecValueData as String: value.data(using: .utf8)!
        ]
        SecItemAdd(query as CFDictionary, nil)
    }

    private func loadFromKeychain(key: String) -> String? {
        let query: [String: Any] = [
            kSecClass as String: kSecClassGenericPassword,
            kSecAttrService as String: service,
            kSecAttrAccount as String: key,
            kSecReturnData as String: true
        ]
        var result: AnyObject?
        let status = SecItemCopyMatching(query as CFDictionary, &result)
        guard status == errSecSuccess, let data = result as? Data else { return nil }
        return String(data: data, encoding: .utf8)
    }

    private func deleteFromKeychain(key: String) {
        let query: [String: Any] = [
            kSecClass as String: kSecClassGenericPassword,
            kSecAttrService as String: service,
            kSecAttrAccount as String: key
        ]
        SecItemDelete(query as CFDictionary)
    }
}

#Preview {
    NavigationStack {
        PreferencesScreen()
    }
}
