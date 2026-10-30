import SwiftUI
import UIKit

// Presents a plain UIKit view controller with present(_:animated:), the way UIKit apps open
// modals, instead of pushing it onto the navigation stack. Elements on a presented controller
// went missing from the iOS UI tree, see https://github.com/mobile-next/mobilewright/issues/262
struct PresentedScreen: View {
    var body: some View {
        List {
            Button("Present Full Screen") { present(.fullScreen) }
            Button("Present Page Sheet") { present(.pageSheet) }
            Button("Present Bottom Sheet") { present(.pageSheet, detents: [.medium()]) }
        }
        .navigationTitle("Presented View Controller")
        .navigationBarTitleDisplayMode(.inline)
    }

    private func present(_ style: UIModalPresentationStyle, detents: [UISheetPresentationController.Detent]? = nil) {
        let controller = PresentedViewController()
        controller.modalPresentationStyle = style
        if let detents {
            controller.sheetPresentationController?.detents = detents
        }
        topViewController()?.present(controller, animated: true)
    }

    private func topViewController() -> UIViewController? {
        let scene = UIApplication.shared.connectedScenes.first { $0 is UIWindowScene } as? UIWindowScene
        var top = scene?.keyWindow?.rootViewController
        while let presented = top?.presentedViewController {
            top = presented
        }
        return top
    }
}

private final class PresentedViewController: UIViewController {
    override func viewDidLoad() {
        super.viewDidLoad()
        view.backgroundColor = .systemBackground

        let title = UILabel()
        title.text = "Presented Screen"
        title.font = .preferredFont(forTextStyle: .title1)
        title.accessibilityIdentifier = "presented-title"

        let body = UILabel()
        body.text = "This view controller was presented modally"
        body.numberOfLines = 0
        body.accessibilityIdentifier = "presented-body"

        let dismissButton = UIButton(type: .system)
        dismissButton.setTitle("Dismiss", for: .normal)
        dismissButton.accessibilityIdentifier = "presented-dismiss"
        dismissButton.addAction(UIAction { [weak self] _ in self?.dismiss(animated: true) }, for: .touchUpInside)

        let stack = UIStackView(arrangedSubviews: [title, body, dismissButton])
        stack.axis = .vertical
        stack.spacing = 16
        stack.translatesAutoresizingMaskIntoConstraints = false
        view.addSubview(stack)
        NSLayoutConstraint.activate([
            stack.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor, constant: 32),
            stack.leadingAnchor.constraint(equalTo: view.layoutMarginsGuide.leadingAnchor),
            stack.trailingAnchor.constraint(equalTo: view.layoutMarginsGuide.trailingAnchor),
        ])
    }
}

#Preview {
    NavigationStack {
        PresentedScreen()
    }
}
