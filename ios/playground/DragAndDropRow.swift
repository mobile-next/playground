import SwiftUI

struct DragAndDropRow: View {
    private static let tapMaxDistance: CGFloat = 10
    private static let hintMessage = "Drag this box into the dotted box on the right."
    private static let scoreMessage = "You score one point."

    @State private var dragOffset = CGSize.zero
    @State private var boxStartFrame = CGRect.zero
    @State private var targetFrame = CGRect.zero
    @State private var alertMessage = ""
    @State private var isAlertShown = false

    var body: some View {
        HStack {
            Rectangle()
                .fill(Color.red)
                .frame(width: 72, height: 72)
                .onGeometryChange(for: CGRect.self) { $0.frame(in: .global) } action: { boxStartFrame = $0 }
                .offset(dragOffset)
                .gesture(dragGesture)
                .accessibilityElement()
                .accessibilityAddTraits(.isButton)
                .accessibilityIdentifier("drag_box")

            Spacer()

            Rectangle()
                .strokeBorder(Color.secondary, style: StrokeStyle(lineWidth: 2, dash: [4, 4]))
                .frame(width: 96, height: 96)
                .onGeometryChange(for: CGRect.self) { $0.frame(in: .global) } action: { targetFrame = $0 }
                .accessibilityElement()
                .accessibilityIdentifier("drop_target")
        }
        .padding(.vertical, 12)
        .zIndex(1)
        .alert(alertMessage, isPresented: $isAlertShown) {
            Button("OK") { returnToStart() }
        }
    }

    private var dragGesture: some Gesture {
        DragGesture(minimumDistance: 0, coordinateSpace: .global)
            .onChanged { dragOffset = $0.translation }
            .onEnded { value in
                let distance = hypot(value.translation.width, value.translation.height)
                if distance < Self.tapMaxDistance {
                    show(Self.hintMessage)
                } else if targetFrame.contains(boxCenter(after: value.translation)) {
                    show(Self.scoreMessage)
                } else {
                    // a box left outside its row can no longer receive touches, so a miss goes home
                    returnToStart()
                }
            }
    }

    private func boxCenter(after translation: CGSize) -> CGPoint {
        CGPoint(x: boxStartFrame.midX + translation.width, y: boxStartFrame.midY + translation.height)
    }

    private func show(_ message: String) {
        alertMessage = message
        isAlertShown = true
    }

    private func returnToStart() {
        withAnimation { dragOffset = .zero }
    }
}

#Preview {
    Form {
        Section("Drag and Drop") {
            DragAndDropRow()
        }
    }
}
