create DATABASE mini_spring_db;

create table users_tb(
                         user_id SERIAL PRIMARY KEY,
                         email VARCHAR(255),
                         password VARCHAR(255),
                         profile_image VARCHAR(255)
);

create table opts_tb(
                        opt_id SERIAL PRIMARY KEY,
                        opt_code VARCHAR(255),
                        issued_at TIMESTAMP DEFAULT LOCALTIMESTAMP,
                        expiration TIMESTAMP,
                        verify BOOLEAN,
                        user_id INT,
                        CONSTRAINT fk_opts_users_id_tb FOREIGN KEY (user_id) REFERENCES users_tb(user_id) on DELETE CASCADE on UPDATE CASCADE
);

create table categories_tb(
                              category_id SERIAL PRIMARY KEY,
                              name VARCHAR(255),
                              description VARCHAR(255),
                              user_id INT,
                              CONSTRAINT fk_categories_users_id_tb FOREIGN KEY (user_id) REFERENCES users_tb(user_id) on DELETE CASCADE ON UPDATE CASCADE
);

CREATE Table expenses_tb(
                            expense_id SERIAL PRIMARY KEY,
                            amount NUMERIC(10,2),
                            description VARCHAR(255),
                            date TIMESTAMP DEFAULT LOCALTIMESTAMP,
                            user_id INT,
                            category_id INT,
                            CONSTRAINT fk_expenses_users_id_tb FOREIGN KEY (user_id) REFERENCES users_tb(user_id) ON DELETE CASCADE ON UPDATE CASCADE,
                            CONSTRAINT fk_expenses_categories_id_tb FOREIGN KEY (category_id) REFERENCES categories_tb(category_id) ON DELETE CASCADE ON UPDATE CASCADE
);
