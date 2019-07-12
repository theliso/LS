use PROJECT_MANAGEMENT;

create table Project(
    id int primary key identity(1,1),
    projectName nvarchar(20) not null,
    description nvarchar(50),
	creationDate datetime not null
);

create table Status(
    id int primary key identity(1,1), 
    description nvarchar(10) not null check (description in ('open', 'close')) 
)

create table Issue(
    idIssue int identity(1,1),
    idProject int,
    idStatus int,
    issueName nvarchar(20) not null,
    description nvarchar(255),
    created datetime not null,
    updated datetime not null,
    primary key(idIssue, idProject),
    foreign key (idProject) references Project(id),
    foreign key (idStatus) references Status(id)
);

create table Comment(
    idComment int identity(1,1),
    idIssue int,
	idProject int, 
    commentDate datetime not null,
    text nvarchar(255) not null,
    primary key(idComment, idIssue, idProject),
    foreign key (idIssue, idProject) references Issue(idIssue, idProject)
);

create table Label(
    id int primary key identity(1,1),
    description nvarchar(30)
);

create table Project_Label(
	idProject int not null,
	idLabel int not null,
	color varchar(10) not null,
	primary key (idProject,idLabel),
	foreign key (idProject) references Project(id),
	foreign key (idLabel) references Label(id)
);

create table Project_Label_Issue(
	idIIssue int not null,
	idProjLab int not null,
	idIProject int not null,
	idPLLabel int not null,
	primary key (idIIssue,idProjLab, idPLLabel,idIProject),
	foreign key (idIIssue,idIProject) references Issue(idIssue,idProject),
	foreign key (idProjLab,idPLLabel) references Project_Label(idProject, idLabel)
);
