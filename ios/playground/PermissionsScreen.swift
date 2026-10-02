import SwiftUI
import AVFoundation
import CoreLocation
import UserNotifications
import Combine

struct PermissionsScreen: View {
    @State private var cameraStatus = ""
    @State private var notificationsStatus = ""
    @StateObject private var location = LocationPermission()

    @State private var alertResult = "No alert shown"
    @State private var showSimpleAlert = false
    @State private var showConfirmAlert = false
    @State private var showThreeButtonAlert = false
    @State private var showPromptAlert = false
    @State private var promptName = ""
    @State private var showActionSheet = false

    private let delayedAlertSeconds = 2.0

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

            Section("Location") {
                Text(location.status)
                    .accessibilityIdentifier("location_permission_status")

                Button("Request Location Permission") {
                    location.request()
                }
                .accessibilityIdentifier("request_location_permission_button")
            }

            Section("Notifications") {
                Text(notificationsStatus)
                    .accessibilityIdentifier("notifications_permission_status")

                Button("Request Notifications Permission") {
                    requestNotificationsPermission()
                }
                .accessibilityIdentifier("request_notifications_permission_button")
            }

            Section("Alerts") {
                Text(alertResult)
                    .accessibilityIdentifier("alert_result")

                Button("Show Simple Alert") { showSimpleAlert = true }
                    .accessibilityIdentifier("show_simple_alert_button")
                Button("Show Confirm Alert") { showConfirmAlert = true }
                    .accessibilityIdentifier("show_confirm_alert_button")
                Button("Show Three Button Alert") { showThreeButtonAlert = true }
                    .accessibilityIdentifier("show_three_button_alert_button")
                Button("Show Prompt Alert") {
                    promptName = ""
                    showPromptAlert = true
                }
                .accessibilityIdentifier("show_prompt_alert_button")
                Button("Show Alert in 2 Seconds") {
                    alertResult = "Waiting for alert"
                    DispatchQueue.main.asyncAfter(deadline: .now() + delayedAlertSeconds) {
                        showSimpleAlert = true
                    }
                }
                .accessibilityIdentifier("show_delayed_alert_button")
                Button("Show Action Sheet") { showActionSheet = true }
                    .accessibilityIdentifier("show_action_sheet_button")
            }
        }
        .navigationTitle("Permissions")
        .navigationBarTitleDisplayMode(.inline)
        .onAppear {
            updateCameraStatus()
            updateNotificationsStatus()
        }
        .alert("Simple Alert", isPresented: $showSimpleAlert) {
            Button("OK") { alertResult = "OK" }
        } message: {
            Text("This is a simple alert")
        }
        .alert("Confirm Alert", isPresented: $showConfirmAlert) {
            Button("Cancel", role: .cancel) { alertResult = "Cancel" }
            Button("OK") { alertResult = "OK" }
        } message: {
            Text("Do you want to continue?")
        }
        .alert("Three Button Alert", isPresented: $showThreeButtonAlert) {
            Button("Yes") { alertResult = "Yes" }
            Button("No") { alertResult = "No" }
            Button("Later", role: .cancel) { alertResult = "Later" }
        } message: {
            Text("Pick one of three options")
        }
        .confirmationDialog("Choose a Color", isPresented: $showActionSheet, titleVisibility: .visible) {
            Button("Red") { alertResult = "Red" }
            Button("Green") { alertResult = "Green" }
            Button("Blue") { alertResult = "Blue" }
            Button("Cancel", role: .cancel) { alertResult = "Cancel" }
        } message: {
            Text("Pick a color for the sheet")
        }
        .alert("Prompt Alert", isPresented: $showPromptAlert) {
            TextField("Name", text: $promptName)
                .accessibilityIdentifier("prompt_alert_input")
            Button("Cancel", role: .cancel) { alertResult = "Cancel" }
            Button("OK") { alertResult = "Hello, \(promptName)" }
        } message: {
            Text("What is your name?")
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

    private func updateNotificationsStatus() {
        UNUserNotificationCenter.current().getNotificationSettings { settings in
            let status: String
            switch settings.authorizationStatus {
            case .authorized, .provisional, .ephemeral:
                status = "Granted"
            case .denied:
                status = "Denied"
            case .notDetermined:
                status = "Not Determined"
            @unknown default:
                status = "Unknown"
            }
            DispatchQueue.main.async {
                notificationsStatus = status
            }
        }
    }

    private func requestNotificationsPermission() {
        UNUserNotificationCenter.current().requestAuthorization(options: [.alert, .sound, .badge]) { _, _ in
            updateNotificationsStatus()
        }
    }
}

class LocationPermission: NSObject, ObservableObject, CLLocationManagerDelegate {
    @Published var status = ""
    private let manager = CLLocationManager()

    override init() {
        super.init()
        manager.delegate = self
    }

    func request() {
        manager.requestWhenInUseAuthorization()
    }

    func locationManagerDidChangeAuthorization(_ manager: CLLocationManager) {
        switch manager.authorizationStatus {
        case .authorizedWhenInUse, .authorizedAlways:
            status = "Granted"
        case .denied:
            status = "Denied"
        case .restricted:
            status = "Restricted"
        case .notDetermined:
            status = "Not Determined"
        @unknown default:
            status = "Unknown"
        }
    }
}

#Preview {
    NavigationStack {
        PermissionsScreen()
    }
}
