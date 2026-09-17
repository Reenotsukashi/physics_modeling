public class InterferenceModel {
    public static double[] calculateIntensity(int N, double a, double d, double lambda, double L, int numPoints, double screenWidth) {
        double[] intensity = new double[numPoints];
        
        double aMeters = a * 1e-6;
        double dMeters = d * 1e-6;
        double lambdaMeters = lambda * 1e-9;
        
        double dx = screenWidth / numPoints;
        
        for (int i = 0; i < numPoints; i++) {
            double x = (i - numPoints / 2.0) * dx;
            double theta = Math.atan(x / L);
            
            double delta = 2 * Math.PI * dMeters * Math.sin(theta) / lambdaMeters;
            double beta = 2 * Math.PI * aMeters * Math.sin(theta) / lambdaMeters;
            
            double interference;
            if (Math.abs(Math.sin(delta / 2)) < 1e-10) {
                interference = N * N;
            } else {
                interference = Math.pow(Math.sin(N * delta / 2) / Math.sin(delta / 2), 2);
            }
            
            double diffraction;
            if (Math.abs(beta / 2) < 1e-10) {
                diffraction = 1.0;
            } else {
                diffraction = Math.pow(Math.sin(beta / 2) / (beta / 2), 2);
            }
            
            intensity[i] = interference * diffraction;
        }
        
        return intensity;
    }

    public static double[] calculateIntensityQuasi(int N, double a, double d, double lambda0, double deltaLambda, double L, int numPoints, double screenWidth) {
        double[] intensity = new double[numPoints];
        int numWavelengths = 50;
        
        double aMeters = a * 1e-6;
        double dMeters = d * 1e-6;
        double lambda0Meters = lambda0 * 1e-9;
        double deltaLambdaMeters = deltaLambda * 1e-9;
        
        double dx = screenWidth / numPoints;
        
        for (int i = 0; i < numPoints; i++) {
            double x = (i - numPoints / 2.0) * dx;
            double theta = Math.atan(x / L);
            
            double sumIntensity = 0.0;
            for (int j = 0; j < numWavelengths; j++) {
                double lambda = lambda0Meters - deltaLambdaMeters / 2 + 
                                j * deltaLambdaMeters / (numWavelengths - 1);
                
                double delta = 2 * Math.PI * dMeters * Math.sin(theta) / lambda;
                double beta = 2 * Math.PI * aMeters * Math.sin(theta) / lambda;
                
                double interference;
                if (Math.abs(Math.sin(delta / 2)) < 1e-10) {
                    interference = N * N;
                } else {
                    interference = Math.pow(Math.sin(N * delta / 2) / Math.sin(delta / 2), 2);
                }
                
                double diffraction;
                if (Math.abs(beta / 2) < 1e-10) {
                    diffraction = 1.0;
                } else {
                    diffraction = Math.pow(Math.sin(beta / 2) / (beta / 2), 2);
                }
                
                sumIntensity += interference * diffraction;
            }
            
            intensity[i] = sumIntensity / numWavelengths;
        }
        
        return intensity;
    }
}