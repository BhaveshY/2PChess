package application;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Main Spring Boot application class for the chess game.
 * 
 * <p>This class serves as the entry point for the Spring Boot web application
 * and configures:
 * <ul>
 *   <li>Component scanning for service layer</li>
 *   <li>Component scanning for controllers</li>
 *   <li>Auto-configuration of Spring Boot features</li>
 *   <li>Web server initialization</li>
 * </ul>
 * 
 * <p>The application uses Spring Boot's auto-configuration to:
 * <ul>
 *   <li>Set up the web environment</li>
 *   <li>Configure the component scan paths</li>
 *   <li>Initialize required beans</li>
 *   <li>Start the embedded web server</li>
 * </ul>
 * 
 * @see GameController
 * @see IGameInterface
 * @version 1.0
 */
@ComponentScan(basePackages = "service")
@ComponentScan(basePackages = "application.controller")
@SpringBootApplication
public class SpringApplication {
    
    /**
     * Main entry point for the Spring Boot application.
     * 
     * <p>This method:
     * <ul>
     *   <li>Initializes the Spring application context</li>
     *   <li>Starts the embedded web server</li>
     *   <li>Configures all auto-configuration beans</li>
     *   <li>Makes the application ready to serve web requests</li>
     * </ul>
     * 
     * @param args Command line arguments passed to the application
     */
    public static void main(String[] args) {
        org.springframework.boot.SpringApplication.run(SpringApplication.class, args);
    }
}
