import SwiftUI
import Combine

struct ContinuousAnimationScreen: View {
    @State private var rotationAngle: Double = 0

    let timer = Timer.publish(every: 1.0 / 60.0, on: .main, in: .common).autoconnect()

    var body: some View {
        Rectangle()
            .fill(Color.red)
            .frame(width: 100, height: 100)
            .rotationEffect(.degrees(rotationAngle))
            .frame(maxWidth: .infinity, maxHeight: .infinity)
            .onReceive(timer) { _ in
                rotationAngle += 360.0 / 60.0
            }
        .navigationTitle("Continuous Animation")
        .navigationBarTitleDisplayMode(.inline)
        .toolbarBackground(Color.black, for: .navigationBar)
        .toolbarColorScheme(.dark, for: .navigationBar)
    }
}

#Preview {
    NavigationStack {
        ContinuousAnimationScreen()
    }
}
