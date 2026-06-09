import SwiftUI
import AVFoundation

struct PermissionsScreen: View {
    @State private var cameraStatus = ""

    var body: some View {
        Form {
            Section("Camera") {
                Text(cameraStatus)
                    .accessibilityIdentifier("camera_permission_status")

                Button("Request Camera Permission") {
                    requestCameraPermission()
                }
                .accessibilityIdentifier("request_camera_permission_button")
            }
        }
        .navigationTitle("Permissions")
        .navigationBarTitleDisplayMode(.inline)
        .onAppear {
            updateCameraStatus()
        }
    }

    private func updateCameraStatus() {
        switch AVCaptureDevice.authorizationStatus(for: .video) {
        case .authorized:
            cameraStatus = "Granted"
        case .denied:
            cameraStatus = "Denied"
        case .restricted:
            cameraStatus = "Restricted"
        case .notDetermined:
            cameraStatus = "Not Determined"
        @unknown default:
            cameraStatus = "Unknown"
        }
    }

    private func requestCameraPermission() {
        AVCaptureDevice.requestAccess(for: .video) { _ in
            DispatchQueue.main.async {
                updateCameraStatus()
            }
        }
    }
}

#Preview {
    NavigationStack {
        PermissionsScreen()
    }
}
