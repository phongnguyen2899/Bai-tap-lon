USE [NoteApp]
GO
/****** Object:  Table [dbo].[account]    Script Date: 11/19/2020 8:08:41 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[account](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[username] [nvarchar](50) NULL,
	[password] [nvarchar](50) NULL,
	[email] [nvarchar](50) NULL,
	[status] [bit] NULL,
	[code] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[note]    Script Date: 11/19/2020 8:08:42 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[note](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[userid] [int] NULL,
	[title] [nvarchar](250) NULL,
	[content] [nvarchar](max) NULL,
	[gps] [nvarchar](250) NULL,
	[createdate] [datetime] NULL,
	[img] [nvarchar](max) NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]

GO
/****** Object:  Table [dbo].[noteimage]    Script Date: 11/19/2020 8:08:42 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[noteimage](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[noteid] [int] NULL,
	[img] [nvarchar](250) NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
ALTER TABLE [dbo].[note]  WITH CHECK ADD  CONSTRAINT [fk_userid] FOREIGN KEY([userid])
REFERENCES [dbo].[account] ([id])
GO
ALTER TABLE [dbo].[note] CHECK CONSTRAINT [fk_userid]
GO
ALTER TABLE [dbo].[noteimage]  WITH CHECK ADD  CONSTRAINT [fk_noteid] FOREIGN KEY([noteid])
REFERENCES [dbo].[note] ([id])
GO
ALTER TABLE [dbo].[noteimage] CHECK CONSTRAINT [fk_noteid]
GO
