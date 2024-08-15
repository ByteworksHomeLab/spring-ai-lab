package com.byteworksinc.airbnb.etl;

import com.byteworksinc.airbnb.entities.Listing;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.fn.supplier.file.FileSupplierProperties;
import org.springframework.cloud.function.context.FunctionCatalog;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class IngestionService {
    private static final Logger log = LoggerFactory.getLogger(IngestionService.class);

    private final FunctionCatalog catalog;
    private final FileSupplierProperties fileSupplierProperties;

    public IngestionService(final FunctionCatalog catalog, final FileSupplierProperties fileSupplierProperties) {
        this.catalog = catalog;
        this.fileSupplierProperties = fileSupplierProperties;
    }

    public void ingest() {
        Runnable composedFunction = catalog.lookup(null);
        composedFunction.run();
    }

    public File getIngestionDirectory() {
        return fileSupplierProperties.getDirectory();
    }

    /**
     * The purpose of this method is to write Airbnb listing to a JSON file for embedding.
     * In the real world, this might be an event-driven flow kicked off when a host creates a new listing.
     * The file is written to a directory watched by a Spring Cloud Function File Supplier.
     *
     * @param listings - The Airbnb Lists to be written to the JSON File
     */
    public void writeJSONFile(List<Listing> listings) {
        if (listings != null && !listings.isEmpty()) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                File ingestionDir = getIngestionDirectory();
                String jsonArray
                        = objectMapper.writeValueAsString(listings);
                File file = new File(ingestionDir, UUID.randomUUID() + ".json");
                try (FileOutputStream output = new FileOutputStream(file)) {
                    byte[] array = jsonArray.getBytes();
                    output.write(array);
                    output.close();
                    log.info("Wrote file {} with {} listings", file.getName(), listings.size());
                } catch (IOException e) {
                    log.error("Could not write JSON to temporary directory", e);
                }
            } catch (JsonProcessingException e) {
                log.error("Could not map Listing", e);
            } catch (RuntimeException e) {
                log.error("Could not write JSON listing");
            }
        }
    }
}
