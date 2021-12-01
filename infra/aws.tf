provider "aws" {
  region = "eu-west-1"
}
resource "aws_s3_bucket" "bucket" {
  bucket = "pgr301-ruda003-terraform"
}

resource "aws_ecr_repository" "ecrrepo" {
  name = "ruda003"
}