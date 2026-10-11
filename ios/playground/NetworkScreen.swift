import SwiftUI
import Alamofire

// The app's own URLSession, built at launch the way most apps keep one around, so a tool
// that attaches later meets a session that already exists. URLSession.shared is the
// other common case.
enum NetworkClients {
    static let customSession: URLSession = {
        let configuration = URLSessionConfiguration.default
        configuration.timeoutIntervalForRequest = 15
        return URLSession(configuration: configuration)
    }()

    static let getURL = URL(string: "https://jsonplaceholder.typicode.com/todos/1")!
    static let httpGetURL = URL(string: "http://jsonplaceholder.typicode.com/todos/1")!
    static let postURL = URL(string: "https://jsonplaceholder.typicode.com/posts")!
    static let postBody = #"{"title":"playground","body":"hello","userId":1}"#
}

// Sends HTTP requests through different URLSession styles and shows the outcome in one
// text, so a test can tell which of them a network tool sees. The result reads
// "<stack> <METHOD> <status> <body>" or "<stack> <METHOD> error: <message>".
struct NetworkScreen: View {
    @State private var result = "idle"

    private static let maxBodyChars = 300

    var body: some View {
        VStack(alignment: .leading, spacing: 16) {
            Button("URLSession.shared GET") { sharedGet() }
            Button("URLSession HTTP GET") { sessionGet(NetworkClients.httpGetURL, label: "session HTTP GET") }
            Button("Alamofire HTTPS GET") { alamofireGet(NetworkClients.getURL, label: "alamofire HTTPS GET") }
            Button("Alamofire HTTP GET") { alamofireGet(NetworkClients.httpGetURL, label: "alamofire HTTP GET") }
            Button("URLSession.shared POST") { Task { await sharedPost() } }
            Button("Custom session GET") { customGet() }

            Text(result)
                .font(.system(.body, design: .monospaced))
                .accessibilityIdentifier("network-result")
                .accessibilityLabel(result)
            Spacer()
        }
        .frame(maxWidth: .infinity, alignment: .leading)
        .padding()
        .navigationTitle("Network")
        .navigationBarTitleDisplayMode(.inline)
    }

    // completion-handler style on the shared session
    private func sharedGet() {
        sessionGet(NetworkClients.getURL, label: "shared GET")
    }

    private func sessionGet(_ url: URL, label: String) {
        result = "\(label) pending"
        URLSession.shared.dataTask(with: url) { data, response, error in
            show(label, data: data, response: response, error: error)
        }.resume()
    }

    private func alamofireGet(_ url: URL, label: String) {
        result = "\(label) pending"
        AF.request(url).responseData { response in
            self.show(label, data: response.data, response: response.response, error: response.error)
        }
    }

    // async/await style on the shared session
    private func sharedPost() async {
        let label = "shared POST"
        result = "\(label) pending"
        var request = URLRequest(url: NetworkClients.postURL)
        request.httpMethod = "POST"
        request.setValue("application/json; charset=utf-8", forHTTPHeaderField: "Content-Type")
        request.httpBody = NetworkClients.postBody.data(using: .utf8)
        do {
            let (data, response) = try await URLSession.shared.data(for: request)
            show(label, data: data, response: response, error: nil)
        } catch {
            show(label, data: nil, response: nil, error: error)
        }
    }

    // a session that existed before anything attached to the app
    private func customGet() {
        let label = "custom GET"
        result = "\(label) pending"
        NetworkClients.customSession.dataTask(with: NetworkClients.getURL) { data, response, error in
            show(label, data: data, response: response, error: error)
        }.resume()
    }

    private func show(_ label: String, data: Data?, response: URLResponse?, error: Error?) {
        let text: String
        if let error {
            text = "\(label) error: \(error.localizedDescription)"
        } else {
            let status = (response as? HTTPURLResponse)?.statusCode ?? 0
            let body = String(decoding: data ?? Data(), as: UTF8.self)
            text = "\(label) \(status) \(compact(body))"
        }
        DispatchQueue.main.async { result = text }
    }

    // One line, so the whole result fits in a single UI tree label.
    private func compact(_ text: String) -> String {
        let oneLine = text.split(whereSeparator: { $0.isWhitespace }).joined(separator: " ")
        return String(oneLine.prefix(Self.maxBodyChars))
    }
}

#Preview {
    NavigationStack {
        NetworkScreen()
    }
}
