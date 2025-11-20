CREATE TABLE stops (
                       id BIGSERIAL PRIMARY KEY,
                       name VARCHAR(255) NOT NULL,
                       stop_order INT NOT NULL,
                       lat DOUBLE PRECISION NOT NULL,
                       lng DOUBLE PRECISION NOT NULL
);

CREATE TABLE routes (
                        id BIGSERIAL PRIMARY KEY,
                        code VARCHAR(50) NOT NULL,
                        name VARCHAR(255),
                        distance_km INT,
                        duration_min INT,
                        origin_id BIGINT NOT NULL REFERENCES stops(id),
                        destination_id BIGINT NOT NULL REFERENCES stops(id)
);
