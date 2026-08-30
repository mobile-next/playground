import Combine
import CoreLocation
import MapKit
import SwiftUI

struct LocationScreen: View {
    @StateObject private var locationProvider = LocationProvider()

    var body: some View {
        VStack(spacing: 0) {
            // ponytail: MapKit needs no api key, unlike google maps on android
            Map(position: $locationProvider.cameraPosition) {
                UserAnnotation()
            }
            .accessibilityIdentifier("map")

            Form {
                Section("Coordinates") {
                    Text(locationProvider.coordinatesText)
                        .accessibilityIdentifier("coordinates")

                    Button("Request Location Permission") {
                        locationProvider.requestPermission()
                    }
                    .accessibilityIdentifier("request_location_permission_button")
                }
            }
            .frame(height: 200)
        }
        .navigationTitle("GPS Location")
        .navigationBarTitleDisplayMode(.inline)
    }
}

class LocationProvider: NSObject, ObservableObject, CLLocationManagerDelegate {
    @Published var coordinatesText = "Unknown"
    @Published var cameraPosition: MapCameraPosition = .userLocation(fallback: .automatic)

    private let manager = CLLocationManager()

    override init() {
        super.init()
        manager.delegate = self
        manager.requestWhenInUseAuthorization()
        manager.startUpdatingLocation()
    }

    func requestPermission() {
        manager.requestWhenInUseAuthorization()
    }

    func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        guard let coordinate = locations.last?.coordinate else { return }
        coordinatesText = String(format: "%.5f, %.5f", coordinate.latitude, coordinate.longitude)
    }

    func locationManager(_ manager: CLLocationManager, didFailWithError error: Error) {
        coordinatesText = error.localizedDescription
    }
}

#Preview {
    NavigationStack {
        LocationScreen()
    }
}
