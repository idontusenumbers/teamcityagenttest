import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.WaitStrategy;
import org.testcontainers.containers.wait.strategy.WaitStrategyTarget;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Duration;


@Testcontainers
class DockerTest {

	private static final Logger log = LoggerFactory.getLogger(DockerTest.class);

	private static GenericContainer<?> sslContainer = null;

	@AfterAll
	public static void dumpLogs() {
		if (sslContainer != null) {
			log.info("SSLContainerLogs:\n{}", sslContainer.getLogs());
			sslContainer.stop();
		}
	}

	@BeforeAll
	public static void setupAll() throws URISyntaxException {

		log.info("Creating SSL Container");

		sslContainer = new GenericContainer<>(DockerImageName.parse("alpine:latest"))
				//.withWorkingDirectory("/openssl-certs")
				//.withCreateContainerCmdModifier(cmd -> cmd.getHostConfig().withBinds(Bind.parse("openssl-certs:/openssl-certs")))
				.withCommand("sleep","10");

		log.info("Starting SSL Container");
		sslContainer.start();

		log.info("Installing OpenSSL in container");
		try {
			log.info("Updating package database");
			sslContainer.execInContainer("apk", "update");

			log.info("Installing OpenSSL");
			sslContainer.execInContainer("apk", "add", "--no-cache" , "openssl");

			log.info("Clearing Cache");
			sslContainer.execInContainer("rm", "-rf", "/var/cache/apk/*");

			log.info("Installation complete");

		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}


	}

	@Test
	public void blankTest() throws IOException {
		
	}


	private static class DontWaitWaitStrategy implements WaitStrategy {
		@Override
		public void waitUntilReady(WaitStrategyTarget waitStrategyTarget) {
			log.info("Container {} is always ready", waitStrategyTarget.getContainerId());
			return;
		}

		@Override
		public WaitStrategy withStartupTimeout(Duration startupTimeout) {
			log.info("Ignoring startup timeout{}", startupTimeout.toString());
			return this;
		}
	}
}
