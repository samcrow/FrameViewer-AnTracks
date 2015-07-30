# Database requirements #

This software requires a relational database server to run in a multi-user environment.

This document describes the requirements. It was current as of version 2.0.2.

## The database server ###

A MySQL server instance is required. Any recent version and any storage engine should work.

### Networking ###

For one person to run Frame Viewer, the database server can run locally on the user's computer. The user can connect to the local server using the address 127.0.0.1.

For multiple people to use Frame Viewer simultaneously, all the computers must be networked together. It is imperative that the MySQL server root password be changed from its default (which allows anyone to log on as root with no password).

Because Frame Viewer synchronously updates everything after it is changed, users may experience seconds-long pauses when connecting to the server over a slow or unreliable connection. Local networking is generally preferred to connecting to the server over the internet.

### Accounts and privileges ###

Although Frame Viewer will work when using the MySQL root account, that would not reflect good security practices. Setting up an account with restricted privileges for all Frame Viewer users is preferable.

A database for Frame Viewer must be created. Frame Viewer will use various tables within that database.

The Frame Viewer account must have the following privileges within the Frame Viewer database:

* `CREATE`
* `DROP`
* `DELETE`
* `INSERT`
* `SELECT`
* `UPDATE`

Frame Viewer uses the `CREATE` and `DROP` privileges to create and drop tables when creating a data set.

#### A more restricted account ####

For users that need to enter data but do not need to create data sets, an account with the following smaller set of privileges could be created:

* `DELETE`
* `INSERT`
* `SELECT`
* `UPDATE`

## Schema ##

This section describes the database schema.

### Data sets ###

Frame Viewer exposes the concept of data sets to the user. Each data set corresponds to two tables: [data-set-name]\_trajectories and [data-set-name]\_points. Because dataset names are incorporated into table names, each data set name must be a valid table name. This seems to mean that it may only contain letters, numbers, and underscores, and that it may not start with a number.

When Frame Viewer connects to the database, it looks at the available tables and uses their names to create a list of data sets. The user can select an existing data set, or type the name of a new data set.

To create a new data set, Frame Viewer first tries to create the [data-set-name]\_trajectories and [data-set-name]\_points tables. The code that does this does not know the intended structure of the tables; it only gives each table a primary key column. If this succeeds, it confirms that the user has entered an acceptable name. Frame Viewer then drops both tables so that the database code can create them with the correct structure.

If the database code detects that both tables are missing, it creates both of them and sets up their schema.

### The trajectories table ###

The trajectories table stores trajectories. Each trajectory has zero or more points, and corresponds to one ant. The following describes the schema:

	CREATE TABLE `data_set_name_trajectories` (
	  `trajectory_id` int(11) NOT NULL,
	  `from_action` varchar(255) NOT NULL DEFAULT 'Unknown',
	  `to_action` varchar(255) NOT NULL DEFAULT 'Unknown',
	  PRIMARY KEY (`trajectory_id`)
	);

`from_action` refers to the Trajectory.FromAction enumerated value. `to_action` refers to the Trajectory.ToAction enumerated value. As usual for Frame Viewer, enums are stored in their string literal formats.

### The points table ###

The points table stores the points that correspond to trajectories. Each point has one trajectory. The following describes the schema:

	CREATE TABLE `data_set_name_points` (
	`point_id` int(11) NOT NULL AUTO_INCREMENT,
	`trajectory_id` int(11) NOT NULL,
	`frame_number` int(11) NOT NULL,
	`frame_x` int(11) NOT NULL,
	`frame_y` int(11) NOT NULL,
	`activity` varchar(255) NOT NULL DEFAULT 'NotCarrying',
	`source` varchar(255) NOT NULL DEFAULT 'User',
	`is_interaction` smallint(6) NOT NULL DEFAULT '0',
	`interaction_met_trajectory_id` int(11) DEFAULT NULL,
	`interaction_type` varchar(255) DEFAULT NULL,
	`interaction_met_ant_activity` varchar(255) DEFAULT NULL,
	PRIMARY KEY (`point_id`)
	);

The column `is_interaction` determines whether the point corresponds to a `Point` object or an `InteractionPoint` object. If this value is 1, the columns that begin with `interaction` must not be null. If this value is 0, those columns can have any value. Their values will be ignored.

The `source` column must be a name of a value of the enum `org.samcrow.frameviewer.trajectory.Point.Source`.

## Future expansion ##

Because the databse code is databse-agnostic and uses JDBC, it could easily be adapted to to other database servers or to SQLite for single-user applications.

#### SQLite/MySQL note ####

[SQLite does not like `AUTO_INCREMENT`](https://www.sqlite.org/autoinc.html). It will automatically add an incremented value to a `PRIMARY KEY` column without `AUTO_INCREMENT`. MySQL, however, requires `AUTO_INCREMENT` and will encounter an error if one attempts to `INSERT` without specifying a value for a column that is `PRIMARY KEY` but not `AUTO_INCREMENT`.
